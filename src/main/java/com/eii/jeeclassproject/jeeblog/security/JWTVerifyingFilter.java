/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eii.jeeclassproject.jeeblog.security;

import com.eii.jeeclassproject.jeeblog.controller.LoginController;
import com.eii.jeeclassproject.jeeblog.dao.UserDao;
import io.jsonwebtoken.ExpiredJwtException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.AccessControlFilter;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 

/**
 *
 * @author winiga
 */
public class JWTVerifyingFilter extends AccessControlFilter {
    
    private static final String SECRET = "Vdic3rj9r0VHnemVmrYSBDFGlnpidEj8kspWqNPc";
    
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
 
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse arg1, Object arg2) {
        
        boolean accessAllowed = false;
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String jwt = httpRequest.getHeader("Authorization");
        String email;
        if (jwt == null || !jwt.startsWith("Bearer ")) {
            return accessAllowed;
        }
        
        
        jwt = jwt.substring(jwt.indexOf(" "));
        String username = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(SECRET))
                .parseClaimsJws(jwt).getBody().getSubject();
        String subjectName = (String) SecurityUtils.getSubject().getPrincipal();
        if (username.equals(subjectName)) {
            accessAllowed = true;
        }
        return accessAllowed;
    }
 
    @Override
    protected boolean onAccessDenied(ServletRequest arg0, ServletResponse arg1) throws Exception {
        HttpServletResponse response = (HttpServletResponse) arg1;
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return false;
    }
 
}