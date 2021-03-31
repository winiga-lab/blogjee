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
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.hibernate.exception.ConstraintViolationException;
import org.omnifaces.util.Messages;

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
        try{
            udao.saveUserWithCrypt(user);
            user = null;
        } catch(ConstraintViolationException ex ) {
            FacesMessage msg = Messages.createError("Un utilisateur est déjà enrégistré sous cet E-mail");
            FacesContext.getCurrentInstance().addMessage("form:signupForm",msg);
            throw new ValidatorException(msg);
        }
        
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    
}
