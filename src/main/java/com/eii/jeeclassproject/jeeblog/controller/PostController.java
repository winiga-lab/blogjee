/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eii.jeeclassproject.jeeblog.controller;

import com.eii.jeeclassproject.jeeblog.dao.PostDao;
import com.eii.jeeclassproject.jeeblog.model.Post;
import com.eii.jeeclassproject.jeeblog.utils.Paginator;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *
 * @author winiga
 */

@Named("posts")
@RequestScoped
public class PostController implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    
    private PostDao postDao;
    
    private int page;
    private int id;
    
    final static int resultPerPage = 3;
    
    
    public PostController() {
        
        postDao = new PostDao();
        
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
        .getRequest();
        
        page = ((request.getParameter("page") != null) && (!request.getParameter("page").isEmpty()) ) ? Integer.valueOf(request.getParameter("page")) : 1;
        
        id = ((request.getParameter("id") != null) && (!request.getParameter("id").isEmpty()) ) ? Integer.valueOf(request.getParameter("id")) : -1;
    }
    
    public List<Post> paginateMixedPost() {
        
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
        .getRequest();
        
        page = ((request.getParameter("page") != null) && (!request.getParameter("page").isEmpty()) ) ? Integer.valueOf(request.getParameter("page")) : 1;
        
        return postDao.getMixedPaginatePosts(page, resultPerPage);
    }
    
    public Post getPostById() {
        
        return postDao.getPostById(id);
    }
    
    public boolean postListHasNext() {
        
        return Paginator.hasNext(page, postDao.totalPage(resultPerPage));
    }
    
    public boolean postListHasPrevious() {
        
        return Paginator.hasPrevious(page);
    }
    
    public int postTotalPage() {
        
        return postDao.totalPage(resultPerPage);
    }

    public int getPage() {
        
        return page;
    }
    
    
}
