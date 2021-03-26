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
import org.apache.shiro.subject.PrincipalCollection;

/**
 *
 * @author winiga
 */
public class AuthenticatedCounter implements AuthenticationListener {

    private static int authCount = 0;

    @Override
    public void onSuccess(AuthenticationToken token, AuthenticationInfo info) {
        authCount++;
    }

    @Override
    public void onFailure(AuthenticationToken token, AuthenticationException ae) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onLogout(PrincipalCollection principals) {
        authCount--;
        SessionCounter.decreaseSessionCount();
    }
    
    public static void decreaseAuthCount() {
        
        authCount--;
    }

    public static int getAuthCount() {
        return authCount;
    }

}
