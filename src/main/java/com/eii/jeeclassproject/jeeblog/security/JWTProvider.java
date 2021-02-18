package com.eii.jeeclassproject.jeeblog.security;


import com.eii.jeeclassproject.jeeblog.dao.UserDao;
import java.io.IOException;
import java.security.Key;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

/**
 * @author winiga
 */
@WebServlet("/JWTProvider")
public class JWTProvider extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
    private static final int ExpireAT = 6 * 3600 * 1000;
    
    private static final String SECRET = "Vdic3rj9r0VHnemVmrYSBDFGlnpidEj8kspWqNPc";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public JWTProvider() throws FileNotFoundException {
        super();
    }

    /**
     * @param request
     * @param response
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        UserDao udao = new UserDao();
        SignatureAlgorithm sigAlg = SignatureAlgorithm.HS512;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, sigAlg.getJcaName());
        JwtBuilder builder = Jwts.builder().setId(String.valueOf(now.getTime())+String.valueOf(signingKey.hashCode()))
                        .setIssuedAt(now)
                        .setExpiration(new Date(now.getTime() + ExpireAT))
                        .setSubject(request.getUserPrincipal().getName())
                        .signWith(sigAlg, signingKey);
        
        response.getWriter().write(builder.compact());
        response.setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            doGet(request, response);
    }

}