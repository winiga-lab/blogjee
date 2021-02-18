/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eii.jeeclassproject.jeeblog.dao;

import com.eii.jeeclassproject.jeeblog.controller.LoginController;
import com.eii.jeeclassproject.jeeblog.model.Post;
import java.util.Collections;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author winiga
 */
@Stateless
public class PostDao {
    
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    
    @PersistenceContext(unitName = "blogPU")
    private  EntityManager em; 
    
    public void getEntityManager() {
        
        if(em == null) {
            EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("blogPU");//
            em = ENTITY_MANAGER_FACTORY.createEntityManager();
        }
    }

    
    public boolean savePost(Post post) {
        
        EntityTransaction tx = em.getTransaction();
        Session session = em.unwrap(Session.class);
        
        try {
            tx.begin();
            
            session.save(post);
            
            tx.commit();
            
            return true;
        } catch(Exception ex) {
            if(tx != null) tx.rollback();
            
            log.error(ex.getMessage(), ex);
            
            return false;
        }
    }
    
    public boolean updatePost(Post post) {
        
        EntityTransaction tx = em.getTransaction();
        Session session = em.unwrap(Session.class);
        
        try {
            tx.begin();
            
            session.update(post);
            
            tx.commit();
            
            return true;
        } catch(Exception ex) {
            if(tx != null) tx.rollback();
            
            log.error(ex.getMessage(), ex);
            
            return false;
        }
    }
    
    public int countPost() {
        
        Session session = em.unwrap(Session.class);
        
        try{
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Long> query = builder.createQuery(Long.class);
            Root<Post> root = query.from(Post.class);
            
            query.select(builder.count(root));
            
            TypedQuery<Long> q = session.createQuery(query);
            
            return  q.getSingleResult().intValue();
        } catch(Exception ex) {
            log.error(ex.getMessage(), ex);
            
            return 0;
        }
    }
    
     public int totalPage(int resultPerPage) {
        
        int resultCount = countPost();
        
        return  (Math.ceil(resultCount/resultPerPage) != 0) ? (int) Math.ceil(resultCount/resultPerPage) : 1;
    }
    
     /**
      * Review
      * @return 
      */
    public List<Post> getPosts() {
        
        Session session = em.unwrap(Session.class);
        
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Post> query = builder.createQuery(Post.class);
            Root<Post> root = query.from(Post.class);
            
            query.select(root);
            
            TypedQuery<Post> q=session.createQuery(query);
            
            return q.getResultList();
        } catch(Exception ex) {
            log.error(ex.getMessage(), ex);
            
            return null;
        }
    }
    
    /**
     * Review
     * @param page
     * @param resultPerPage
     * @return 
     */
    public List<Post> getPaginatePosts(int page, int resultPerPage) {
  
        Session session = em.unwrap(Session.class);
        
        try{
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Post> query = builder.createQuery(Post.class);
            Root<Post> root = query.from(Post.class);
            
            query.select(root);
            
            TypedQuery<Post> q = session.createQuery(query);
            
            q.setFirstResult((page - 1) * resultPerPage);
            q.setMaxResults(resultPerPage);
            return q.getResultList();
        } catch(Exception ex) {
            
            log.error(ex.getMessage(), ex);
            
            return null;
        }
    }
    
    public List<Post> getPaginatePostsWithoutDetails(int page, int resultPerPage) {
  
        Session session = em.unwrap(Session.class);
        
        try{
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Post> query = builder.createQuery(Post.class);
            Root<Post> root = query.from(Post.class);
            
            query.multiselect(root.get("id"), root.get("title"), root.get("resume"), root.get("datePost"), root.get("user"));
            
            TypedQuery<Post> q = session.createQuery(query);
            
            q.setFirstResult((page - 1) * resultPerPage);
            q.setMaxResults(resultPerPage);
            return q.getResultList();
        } catch(Exception ex) {
            
            log.error(ex.getMessage(), ex);
            
            return null;
        }
    }
    
    /**
     * Review
     * @param page
     * @param resultPerPage
     * @return 
     */
    public List<Post> getMixedPaginatePosts(int page, int  resultPerPage) {
        
        List<Post> mixedResult = getPaginatePosts(page, resultPerPage);
        
        Collections.shuffle(mixedResult);
        
        return mixedResult;
    }
    
    public List<Post> getMixedPaginatePostsWithoutDetails(int page, int  resultPerPage) {
        
        List<Post> mixedResult = getPaginatePosts(page, resultPerPage);
        
        Collections.shuffle(mixedResult);
        
        return mixedResult;
    }
    
    public Post getPostById(int id) {
        
        Session session = em.unwrap(Session.class);
        
        try{
            return session.byId(Post.class).getReference(id);
        } catch(Exception ex) {
            log.error(ex.getMessage(), ex);
            
            return null;
        }
    }
    
    public boolean deletePost(Post post) {
        
        EntityTransaction tx = em.getTransaction();
        Session session = em.unwrap(Session.class);
        
        try {
            tx.begin();
            
            session.delete(post);
            
            tx.commit();
            
            return true;
        } catch(Exception ex) {
            if(tx != null) tx.rollback();
            
            log.error(ex.getMessage(), ex);
            
            return false;
        }
    }
    
}
