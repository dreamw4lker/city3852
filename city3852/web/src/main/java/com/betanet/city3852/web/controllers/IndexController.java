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

import com.betanet.city3852.domain.cookieentity.CookieEntity;
import com.betanet.city3852.domain.vehicle.Vehicle;
import com.betanet.city3852.domain.vehicle.VehicleType;
import com.betanet.city3852.service.api.CookieEntityService;
import com.betanet.city3852.service.api.ForecastsService;
import com.betanet.city3852.service.api.StationsService;
import com.betanet.city3852.service.api.VehiclesService;
import java.util.ArrayList;
import java.util.List;
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
    
    @RequestMapping(value = "/markers", method = RequestMethod.GET)
    public String markers(Model model, 
            @RequestParam(name = "vehicleType", required = false) String vehicleTypeString,
            @RequestParam(name = "routeNumber", required = false) String routeNumber) {
        if(routeNumber == null){
            routeNumber = "";
        }
        VehicleType vehicleType;
        if((vehicleTypeString == null) || (vehicleTypeString.equals(""))){
            vehicleType = VehicleType.ALL;
        } else {
            vehicleType = VehicleType.valueOf(vehicleTypeString);
        }
        CookieEntity cookieEntity = cookieEntityService.getCookieEntityByKey("PHPSESSID");
        List<CookieEntity> cookieEntities = new ArrayList<>();
        cookieEntities.add(cookieEntity);
        List<Vehicle> vehiclesListByRouteNumber = vehiclesService.getVehiclesListByRouteNumber(routeNumber, vehicleType, cookieEntities);
        model.addAttribute("vehicles", vehiclesListByRouteNumber);
        return "markers";
    }
    
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test() {
        System.out.println("ssize:" + stationsService.getAll().size());
        stationsService.initStations();
        System.out.println("ssize:" + stationsService.getAll().size());
        return "index";
    }
    
    @RequestMapping("/forecast")
    public String editUser(Model model, @RequestParam(name = "stationId") String stationId,
            @RequestParam(name = "stationType") String stationType) {
        model.addAttribute("station", stationsService.getStationByStationIDAndType(Integer.valueOf(stationId), Integer.valueOf(stationType)));
        model.addAttribute("forecasts", forecastsService.getForecastsByStationIdAndType(Integer.valueOf(stationId), stationType));
        return "forecastModal :: content";
    }
}
