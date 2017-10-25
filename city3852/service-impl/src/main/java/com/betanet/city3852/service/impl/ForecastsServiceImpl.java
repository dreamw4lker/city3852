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

import com.betanet.city3852.domain.vehicle.forecast.Forecast;
import com.betanet.city3852.service.api.ForecastsService;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

/**
 * Forecast creation service
 * 
 * @author Alexander Shkirkov
 */
@Service
public class ForecastsServiceImpl implements ForecastsService {
    
    private static String forecastsDownloadURL = "http://traffic22.ru/php/getStationForecasts.php?";
    
    private JSONArray downloadForecasts(Integer stationId, String type){
        JSONArray forecastArray;
        String urlString = forecastsDownloadURL + "sid=" + stationId + "&type=" + type + "&city=barnaul&info=12345";
        try {
            forecastArray = new JSONArray(IOUtils.toString(new URL(urlString), Charset.forName("UTF-8")));
        } catch (MalformedURLException ex) {
            System.out.println("Error while creating URL instance: " + ex.getMessage());
            return null;
        } catch (IOException ex) {
            System.out.println("Error while connecting to URL: " + ex.getMessage());
            return null;
        }
        return forecastArray;
    }
    
    @Override
    public List<Forecast> getForecastsByStationIdAndType(Integer stationId, String type) {
        JSONArray forecasts = downloadForecasts(stationId, type);
        
        List<Forecast> forecastEntities = new ArrayList<>();
        try {
            for(Object forecast : forecasts){
                Forecast forecastEntity = new Forecast();
                forecastEntity.setArriveMinutes((int)TimeUnit.SECONDS.toMinutes(Integer.parseInt(((JSONObject)forecast).get("arrt").toString()))); //TODO: refactor
                forecastEntity.setRouteNumber(((JSONObject)forecast).get("rnum").toString());
                forecastEntity.setRouteType(((JSONObject)forecast).get("rtype").toString());
                forecastEntities.add(forecastEntity);
            }
        } catch (NumberFormatException | JSONException ex){
            System.out.println("Error while prepating forecasts: " + ex.getMessage());
        }
        return forecastEntities;
    }
}
