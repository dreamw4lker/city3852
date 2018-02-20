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
package com.betanet.city3852.web.controllers;

import com.betanet.city3852.domain.vehicle.Vehicle;
import com.betanet.city3852.service.api.CookieEntityService;
import com.betanet.city3852.service.api.ForecastsService;
import com.betanet.city3852.service.api.StationsService;
import com.betanet.city3852.service.api.VehiclesService;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Main page controller
 * 
 * @author shkirkov.au
 */
@Controller
public class IndexController {
    
    @Autowired
    private VehiclesService vehiclesService;
    
    @Autowired
    private StationsService stationsService;
    
    @Autowired
    private ForecastsService forecastsService;
    
    @Autowired
    private CookieEntityService cookieEntityService;
    
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {
        cookieEntityService.initCookies();
        model.addAttribute("stations", stationsService.getAll());
        return "index";
    }
    
    @Setter
    private static class MarkerParametersData {

        @JsonProperty("parameters")
        List<MarkerParameter> markerParameters;

        public List<Entry<String, String>> getParametersList() {
            List<Entry<String,String>> parametersList = new ArrayList<>();
            markerParameters.forEach((mp) -> {
                parametersList.add(new AbstractMap.SimpleEntry<>(mp.key, mp.value));
            });
            return parametersList;
        }
    }

    @Setter
    @Getter
    private static class MarkerParameter { 
        String key;
        String value;
    }
    
    //TODO: work on IOException
    //TODO: change to POST method?
    @RequestMapping(value = "/markers", method = RequestMethod.GET)
    public String markers(Model model, 
            @RequestParam(name = "parameters", required = false) String parametersString) throws IOException { 
        MarkerParametersData markersData = new ObjectMapper().readValue(parametersString, MarkerParametersData.class);
        
        List<Vehicle> vehiclesListByRouteNumber = vehiclesService.getVehiclesListByRouteNumber(markersData.getParametersList(), cookieEntityService.getAllCookies());
        model.addAttribute("vehicles", vehiclesListByRouteNumber);
        return "markers";
    }
    
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test() {
        System.out.println("ssize:" + stationsService.getAll().size());

        stationsService.initStations(cookieEntityService.getAllCookies());
        System.out.println("ssize:" + stationsService.getAll().size());
        return "index";
    }
    
    @RequestMapping("/forecast")
    public String editUser(Model model, @RequestParam(name = "stationId") String stationId,
            @RequestParam(name = "stationType") String stationType) {
        model.addAttribute("station", stationsService.getStationByStationIDAndType(Integer.valueOf(stationId), Integer.valueOf(stationType)));
        
        model.addAttribute("forecasts", forecastsService.getForecastsByStationIdAndType(Integer.valueOf(stationId), stationType, cookieEntityService.getAllCookies()));
        return "forecastModal :: content";
    }
}
