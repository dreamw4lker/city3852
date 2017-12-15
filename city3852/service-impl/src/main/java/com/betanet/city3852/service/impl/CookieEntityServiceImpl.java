/*
 * The MIT License
 *
 * Copyright 2017 Alexander Shkirkov.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.betanet.city3852.service.impl;

import com.betanet.city3852.domain.cookieentity.CookieEntity;
import com.betanet.city3852.repository.CookieEntityRepository;
import com.betanet.city3852.service.api.CookieEntityService;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Alexander Shkirkov
 */
@Service("cookieEntityService")
public class CookieEntityServiceImpl implements CookieEntityService{
    @Autowired
    private CookieEntityRepository cookieEntityRepository;
    
    private static String cookieCatcherURL = "http://traffic22.ru";

    @Transactional
    @Override
    public void save(CookieEntity cookieEntity) {
        if(cookieEntity == null){
            return;
        }
        CookieEntity oldCookieEntity = cookieEntityRepository.getCookieByKey(cookieEntity.getCookieKey());
        if(oldCookieEntity != null){
            oldCookieEntity.setCookieValue(cookieEntity.getCookieValue());
            oldCookieEntity.setCookieDate(cookieEntity.getCookieDate());
            cookieEntityRepository.save(oldCookieEntity);
        } else {
            cookieEntityRepository.save(cookieEntity);
        }
    }
    
    @Override
    public void initCookies() {
        CookieEntity oldCookie = getCookieEntityByKey("PHPSESSID");
        if((oldCookie == null) || (oldCookie.getCookieDate() != null && (ChronoUnit.HOURS.between(oldCookie.getCookieDate(), LocalDateTime.now()) > 1))){
            try {
                URLConnection connection = new URL(cookieCatcherURL).openConnection();
                String cookieString = connection.getHeaderFields().get("Set-Cookie").get(0);
                for(String oneCookie : cookieString.split(";")){
                    String[] cookieKeyValue = oneCookie.split("=");
                    if(cookieKeyValue.length == 2){
                        CookieEntity cookieEntity = new CookieEntity();
                        cookieEntity.setCookieKey(cookieKeyValue[0].trim());
                        cookieEntity.setCookieValue(cookieKeyValue[1].trim());
                        cookieEntity.setCookieDate(LocalDateTime.now());
                        save(cookieEntity);
                    }
                }
            } catch (IOException ex) {
                System.out.println("Error while creating cookie instances: " + ex.getMessage());
            }
        }
    }

    @Override
    public CookieEntity getCookieEntityByKey(String key) {
        return cookieEntityRepository.getCookieByKey(key);
    }
    
    @Override
    public List<CookieEntity> getAllCookies(){
        return cookieEntityRepository.getAllCookies();
    }
    
}
