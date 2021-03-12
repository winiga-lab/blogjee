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
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 *
 * @author winiga
 */
@Named("author")
@RequestScoped
public class AuthorController implements Serializable{
    
    @EJB
    private UserDao udao;
    
    private String AuthorEmail;
    
    public AuthorController() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
                .getRequest();
        AuthorEmail = request.getParameter("email");
    }
    
    public User getUserInfo() {
        Subject currentUser = SecurityUtils.getSubject();
        
        if(currentUser != null && currentUser.getPrincipal() != null ) {
            return udao.getUserByEmail(currentUser.getPrincipal().toString());
        }
        return null;
    }

}
