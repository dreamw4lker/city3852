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
package com.betanet.city3852.service.api;

import com.betanet.city3852.domain.cookieentity.CookieEntity;
import com.betanet.city3852.domain.station.Station;
import java.util.List;

/**
 *
 * @author Alexander Shkirkov
 */
public interface StationsService {
    /**
     * Save station to DB
     * @param station station to save
     */
    public void save(Station station);
    
    /**
     * Gets all stations
     * @return list of stations
     */
    public List<Station> getAll();
    
    /**
     * Initializing stations table with currently actual stations data
     * @param cookieEntities
     */
    public void initStations(List<CookieEntity> cookieEntities);
    
    /**
     * Gets station with unique id and type
     * @param stationId
     * @param stationType
     * @return station entity
     */
    public Station getStationByStationIDAndType(Integer stationId, Integer stationType);
}
