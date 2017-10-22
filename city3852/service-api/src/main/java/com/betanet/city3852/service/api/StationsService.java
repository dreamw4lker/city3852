/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.betanet.city3852.service.api;

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
}
