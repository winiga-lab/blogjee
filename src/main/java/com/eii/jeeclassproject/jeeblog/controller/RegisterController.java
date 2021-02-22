/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eii.jeeclassproject.jeeblog.controller;

import com.eii.jeeclassproject.jeeblog.dao.UserDao;
import com.eii.jeeclassproject.jeeblog.model.User;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author winiga
 */
@Named("register")
@ViewScoped
public class RegisterController implements Serializable{
    
    private static final long serialVersionUID = 1L;    
    
    User user;
    
    @EJB
    UserDao udao;

    public RegisterController() {
        user = new User();
    }
    
    public void doRegister() {
        udao.saveUserWithCrypt(user);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    
}
