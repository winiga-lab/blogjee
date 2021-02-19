/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eii.jeeclassproject.jeeblog.controller;

import java.io.IOException;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.apache.shiro.SecurityUtils;

/**
 *
 * @author winiga
 */
@Named("sessionControl")
@SessionScoped
public class SessionController implements Serializable{
    
    public boolean isAuthenticated(){
        return SecurityUtils.getSubject().isAuthenticated();
    }
    
    public void disconnect() throws IOException {
        if(SecurityUtils.getSubject() != null) {
            SecurityUtils.getSubject().logout();
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect(ec.getRequestContextPath() + "/");
        }
    }
    
    public void redirectAuthenticated() throws IOException {
        if (SecurityUtils.getSubject().isAuthenticated()) {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect(ec.getRequestContextPath() + "/");
        }
    }
}
