package com.betanet.city3852.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Main page controller
 * 
 * @author shkirkov.au
 */
@Controller
public class IndexController {
    
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "index";
    }
}
