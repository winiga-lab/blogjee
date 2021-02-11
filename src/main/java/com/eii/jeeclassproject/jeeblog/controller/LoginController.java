/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eii.jeeclassproject.jeeblog.controller;

import com.eii.jeeclassproject.jeeblog.dao.UserDao;
import com.eii.jeeclassproject.jeeblog.model.User;
import java.io.IOException;
import java.io.Serializable;
import static java.lang.Math.log;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author winiga
 */
@Named("login")
@ViewScoped
public class LoginController implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    
    private String username;
    private String password;
    private UserDao udao = new UserDao();

    public LoginController(String username, String password) {
        this.username = username;
        this.password = password;
        
    }

    public LoginController() {
        udao = new UserDao();
    }
    
    public void doLogin() {
        Subject subject = SecurityUtils.getSubject();   
        UsernamePasswordToken token = new UsernamePasswordToken(getUsername(), getPassword());

        try {
            subject.login(token);
            Subject currentUser = SecurityUtils.getSubject();
            System.out.println(udao.getUserByEmail(currentUser.getPrincipal().toString()).toString());
            FacesContext.getCurrentInstance().getExternalContext().redirect("pages/welcome.jsf");
        } catch (UnknownAccountException ex) {
            log.error(ex.getMessage(), ex);
        } catch (IncorrectCredentialsException | LockedAccountException ex) {
            log.error(ex.getMessage(), ex);
        } catch (AuthenticationException | IOException ex) {
            log.error(ex.getMessage(), ex);
        } finally {
            token.clear();
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
