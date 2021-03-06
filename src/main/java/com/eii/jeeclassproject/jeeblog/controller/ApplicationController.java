/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eii.jeeclassproject.jeeblog.controller;

import java.util.HashMap;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 *
 * @author winiga
 */
@ApplicationScoped
@Named("application")
public class ApplicationController {
    
    private static HashMap<String, Integer> visitCount = new HashMap<String,Integer>();;
    
    //private static HashMap<String, Integer> navigatorCount;
    
    public void setVisit(String key) {
        visitCount.putIfAbsent(key, 1);
    }
    
    public int getTotalVisit() {
        int totalVisit = 0;
        if(!visitCount.isEmpty()) {
            totalVisit = visitCount.entrySet().stream().map(m -> Integer.valueOf(m.getValue().toString())).reduce(totalVisit, Integer::sum); 
        }
        return totalVisit;
    }
}
