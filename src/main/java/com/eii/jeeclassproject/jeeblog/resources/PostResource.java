/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eii.jeeclassproject.jeeblog.resources;

import com.eii.jeeclassproject.jeeblog.controller.LoginController;
import com.eii.jeeclassproject.jeeblog.dao.PostDao;
import com.eii.jeeclassproject.jeeblog.dao.UserDao;
import com.eii.jeeclassproject.jeeblog.model.Post;
import java.util.Date;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.ws.http.HTTPException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author winiga
 */
@Path("post")
public class PostResource {
    
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    
    @EJB
    private PostDao postDao ;
    @EJB
    private UserDao udao;
    
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPost(Post post) {
        
        Subject currentUser = SecurityUtils.getSubject();
        
        try{
            post.setUser(udao.getUserByEmail(currentUser.getPrincipal().toString()));
            post.setDraft(Short.valueOf("1"));
            Post editPost = postDao.getPostByTitle(post.getTitle());
            if(editPost != null && editPost.getTitle() != null) {
                editPost.setResume(post.getResume());
                editPost.setDetails(post.getDetails());
                editPost.setIsEdited(Short.valueOf("1"));
                editPost.setDateEdition(new Date(System.currentTimeMillis()));
                return postDao.updatePost(editPost) ? Response.ok().build() : Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
            }
            return postDao.savePost(post) ? Response.ok().build() : Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
        }catch(HTTPException ex) {
            log.error(ex.getMessage());
            
            return Response.status(ex.getStatusCode()).entity(ex.getMessage()).build();
        }catch(Exception ex) {
            log.error(ex.getMessage());
            
            return Response.status(Response.Status.NOT_FOUND).entity(ex.getMessage()).build();
        }
    }
    
    @PATCH
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePost(@PathParam("id") String id, @FormParam("details") String details) {
        
        
        try{
            Post post = postDao.getPostById(Integer.valueOf(id));
            post.setDetails(details);
            return postDao.updatePost(post) ? Response.ok().build() : Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
        }catch(HTTPException ex) {
            log.error(ex.getMessage());
            
            return Response.status(ex.getStatusCode()).entity(ex.getMessage()).build();
        }catch(NumberFormatException ex) {
            log.error(ex.getMessage());
            
            return Response.status(Response.Status.NOT_FOUND).entity(ex.getMessage()).build();
        }
    }
    
}
