/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eii.jeeclassproject.jeeblog.weblistner;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationListener;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author winiga
 */
public class SessionCounter implements SessionListener {

    private static final Logger log = LoggerFactory.getLogger(SessionCounter.class);
    
    private static int count = 0;
    

    @Override
    public void onStart(Session session) {
        count++;
    }
    
    @Override
    public void onExpiration(Session session) {
        count--;
    }

    @Override
    public void onStop(Session session) {
        log.info("Has stopped");
    }
    
    public static void decreaseSessionCount() {
        
        count--;
    }
    
    public static int getCount() {
        return count;
    }
    
    
}