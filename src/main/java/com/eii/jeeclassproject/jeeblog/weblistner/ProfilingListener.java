/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eii.jeeclassproject.jeeblog.weblistner;

import com.eii.jeeclassproject.jeeblog.controller.ApplicationController;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author winiga
 */
@WebListener
public class ProfilingListener implements ServletRequestListener {

    @Inject
    @Named("application")
    private ApplicationController app;
    
    @Override
    public void requestInitialized(ServletRequestEvent event) {
        ServletRequest s = event.getServletRequest();
        HttpServletRequest httpRequest = (HttpServletRequest) s;        
        if(httpRequest.getMethod().equalsIgnoreCase("GET")){
            //System.out.println(httpRequest.getHeader("User-Agent"));
            if(app != null) {
                app.setVisit(httpRequest.getRemoteAddr());
                //System.out.println(app.getTotalVisit());
            }
        }
    }
    
//
//    @Override
//    public void requestDestroyed(ServletRequestEvent event) {
//        System.out.println("Request destroyed");
//    }

}