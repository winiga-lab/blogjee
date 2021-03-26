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
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
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

    @EJB
    private PostDao postDao;

    private int page;
    private int id;
    private String title;

    final static int resultPerPage = 3;

    public PostController() {

        postDao = new PostDao();

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
                .getRequest();

        page = ((request.getParameter("page") != null) && (!request.getParameter("page").isEmpty())) ? Integer.valueOf(request.getParameter("page")) : 1;

        id = ((request.getParameter("id") != null) && (!request.getParameter("id").isEmpty())) ? Integer.valueOf(request.getParameter("id")) : -1;

        title = request.getParameter("title");
    }

    public List<Post> paginateMixedPost() {
//        postDao.getCreatedByDate();
        return postDao.getPaginatePublished(page, resultPerPage);
    }

    private void increaseViewsCount(Post post) {

        Subject currentUser = SecurityUtils.getSubject();
        //update views of the given post
        Session session = currentUser.getSession();
        if (session.getAttribute(post.getTitle()) == null
                && post.getUser() != null
                && (currentUser.getPrincipal() == null ? true : !post.getUser().getEmail().equals(currentUser.getPrincipal().toString())) 
                && post.getDraft() == 0) {
            session.setAttribute(post.getTitle(), true);
            postDao.incrementPostViewCount(title);
        }
    }
    
    public void changePublishedState() {
        
        if(title != null && !title.isEmpty()) {
            Post p = postDao.getPostByTitle(title);
            p.setDraft(p.getDraft() != 0 ? Short.valueOf("0") : Short.valueOf("1"));
            postDao.updatePost(p);
        }
        
    }

    public Post getPostById() {

        return postDao.getPostById(id);
    }

    public Post getPostByTitle() {

        if (title != null && !title.isEmpty()) {
            Post p = postDao.getPostByTitle(title);
            increaseViewsCount(p);
            return p;
        }
        return null;
    }

    public boolean postListHasNext() {

        return Paginator.hasNext(page, postTotalPage());
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
