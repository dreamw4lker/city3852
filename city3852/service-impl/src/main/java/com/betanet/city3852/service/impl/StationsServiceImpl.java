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

import com.betanet.city3852.domain.station.Station;
import com.betanet.city3852.repository.StationsRepository;
import com.betanet.city3852.service.api.StationsService;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import javax.transaction.Transactional;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Alexander Shkirkov
 */
@Service("stationsService")
public class StationsServiceImpl implements StationsService {
    
    @Autowired
    private StationsRepository stationsRepository;
    
    private static String stationsDownloadURL = "http://traffic22.ru/php/getStations.php?city=barnaul";

    private JSONArray downloadStations(){
        JSONArray stationsArray;
        try {
            stationsArray = new JSONArray(IOUtils.toString(new URL(stationsDownloadURL), Charset.forName("UTF-8")));
        } catch (MalformedURLException ex) {
            System.out.println("Error while creating URL instance: " + ex.getMessage());
            return null;
        } catch (IOException ex) {
            System.out.println("Error while connecting to URL: " + ex.getMessage());
            return null;
        }
        return stationsArray;
    }
    
    @Override
    public void initStations(){
        JSONArray stations = downloadStations();
        
        try {
            for(Object station : stations){
                Station stationEntity = new Station();
                stationEntity.setStationId(Integer.parseInt(((JSONObject)station).get("id").toString()));
                stationEntity.setStationName(((JSONObject)station).get("name").toString() 
                        + " " + ((JSONObject)station).get("descr").toString());
                stationEntity.setStationType(Integer.parseInt(((JSONObject)station).get("type").toString()));
                stationEntity.setLatitude(((JSONObject)station).get("lat").toString());
                stationEntity.setLongtitude(((JSONObject)station).get("lng").toString());
                
                save(stationEntity);
            }
        } catch (NumberFormatException | JSONException ex){
            System.out.println("Error while creating or saving stations entity: " + ex.getMessage());
        }
    }
    
    @Transactional
    @Override
    public void save(Station station) {
        stationsRepository.save(station);
    }

    @Override
    public List<Station> getAll() {
        return stationsRepository.getAll();
    }
}
