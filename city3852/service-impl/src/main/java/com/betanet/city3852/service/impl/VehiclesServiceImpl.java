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
import com.betanet.city3852.domain.vehicle.Vehicle;
import com.betanet.city3852.domain.vehicle.VehicleType;
import com.betanet.city3852.service.api.VehiclesService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

/**
 * Vehicle entity creation service
 * 
 * @author Alexander Shkirkov
 */
@Service
@Slf4j
public class VehiclesServiceImpl implements VehiclesService {

    private static String URLHead = "http://traffic22.ru";
    private static String vehiclesDownloadURL = URLHead + "/php/getVehiclesMarkers.php?rids="
            + "8-0,9-0,20-0,21-0,19-1,20-1,50-0,51-0,11-1,12-1,4-1,5-1,9-1,10-1,8-1,7-1,13-0,12-0,67-0,68-0,15-0,14-0,13-1,14-1,"
            + "16-1,15-1,69-0,70-0,18-1,17-1,64-0,63-0,2-1,3-1,25-0,24-0,120-0,119-0,100-0,99-0,101-0,102-0,40-0,41-0,42-0,43-0,"
            + "104-0,103-0,55-0,54-0,122-0,121-0,146-0,145-0,48-0,49-0,94-0,93-0,17-0,16-0,19-0,18-0,106-0,105-0,91-0,92-0,32-0,"
            + "33-0,45-0,44-0,62-0,61-0,38-0,39-0,27-0,26-0,23-0,22-0,112-0,111-0,10-0,11-0,140-0,139-0,135-0,136-0,113-0,114-0,"
            + "96-0,95-0,131-0,132-0,143-0,144-0,97-0,98-0,34-0,35-0,29-0,28-0,52-0,53-0,327-0,83-0,84-0,326-0,75-0,76-0,87-0,"
            + "88-0,71-0,72-0,319-0,318-0,329-0,328-0,74-0,73-0,325-0,324-0,321-0,320-0,313-0,312-0,314-0,315-0,141-0,142-0,"
            + "133-0,134-0,317-0,316-0,322-0,323-0,81-0,82-0,79-0,80-0,57-0,56-0,86-0,85-0"
            + "&lat0=0&lng0=0&lat1=90&lng1=180&curk=0&city=barnaul&info=12345";

    private JSONArray downloadVehiclesMarkers(List<CookieEntity> cookieEntities){
        JSONObject json;
        String cookieString = "";
        cookieString = cookieEntities.stream().map((CookieEntity entity) -> entity.getCookieKey() + "=" + entity.getCookieValue() + ";").reduce(cookieString, String::concat);
        try {
            URLConnection urlConn = new URL(vehiclesDownloadURL).openConnection();
            urlConn.setRequestProperty("Cookie", cookieString); 
            urlConn.connect();
            json = new JSONObject(IOUtils.toString(urlConn.getInputStream(), Charset.forName("UTF-8")));
        } catch (MalformedURLException ex) {
            System.out.println("Error while creating URL instance: " + ex.getMessage());
            return null;
        } catch (IOException ex) {
            System.out.println("Error while connecting to URL: " + ex.getMessage());
            return null;
        }
        return json.getJSONArray("anims");
    }
    
    @Override
    public List<Vehicle> getVehiclesListByRouteNumber(List<Entry<String, String>> markersData, List<CookieEntity> cookieEntities) {
        JSONArray vehicles = downloadVehiclesMarkers(cookieEntities);
        
        List<Vehicle> vehicleEntities = new ArrayList<>();
        try {
            for(Object vehicle : vehicles){
                if(markersData.isEmpty() || markersData.contains(new AbstractMap.SimpleEntry<>(
                        ((JSONObject)vehicle).get("rnum").toString(),
                        VehicleType.getVehicleTypeByValue(((JSONObject)vehicle).get("rtype").toString())))){
                    Vehicle vehicleEntity = new Vehicle();
                    vehicleEntity.setLatitude(((JSONObject)vehicle).get("lat").toString());
                    vehicleEntity.setLongtitude(((JSONObject)vehicle).get("lon").toString());
                    vehicleEntity.setRegNumber(((JSONObject)vehicle).get("gos_num").toString());
                    vehicleEntity.setRouteNumber(((JSONObject)vehicle).get("rnum").toString());
                    vehicleEntity.setRouteType(((JSONObject)vehicle).get("rtype").toString());
                    vehicleEntity.setSpeed(Integer.parseInt(((JSONObject)vehicle).get("speed").toString()));
                    vehicleEntity.setDir(Integer.parseInt(((JSONObject)vehicle).get("dir").toString()));
                    vehicleEntities.add(vehicleEntity);
                }
            }
        } catch (NumberFormatException | JSONException ex){
            System.out.println("Error while prepating vehicles list: " + ex.getMessage());
        }
        return vehicleEntities;
        
    }
}
