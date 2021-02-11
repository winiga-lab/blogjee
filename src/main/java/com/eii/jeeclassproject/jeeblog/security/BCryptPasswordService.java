/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eii.jeeclassproject.jeeblog.security;


import org.apache.shiro.authc.credential.PasswordService;
import org.springframework.security.crypto.bcrypt.BCrypt;


/**
 *
 * @author winiga
 */
public class BCryptPasswordService implements PasswordService {
 
    @Override
    public String encryptPassword(Object plaintextPassword) throws IllegalArgumentException {
        final String str;
        if (plaintextPassword instanceof char[]) {
            str = new String((char[]) plaintextPassword);
        } else if (plaintextPassword instanceof String) {
            str = (String) plaintextPassword;
        } else {
            throw new IllegalArgumentException("Unsupported password type: " + plaintextPassword.getClass().getName());
        }
        return BCrypt.hashpw(str, BCrypt.gensalt());
    }
 
    @Override
    public boolean passwordsMatch(Object submittedPlaintext, String encrypted) {
        return BCrypt.checkpw(new String((char[]) submittedPlaintext), encrypted);
    }
 
    public boolean passwordsMatchString(String submittedPlaintext, String encrypted) {
        return BCrypt.checkpw( submittedPlaintext, encrypted);
    }
}