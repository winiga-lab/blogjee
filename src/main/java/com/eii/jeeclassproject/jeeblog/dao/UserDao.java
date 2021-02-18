/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eii.jeeclassproject.jeeblog.dao;

import com.eii.jeeclassproject.jeeblog.controller.LoginController;
import com.eii.jeeclassproject.jeeblog.model.User;
import com.eii.jeeclassproject.jeeblog.security.BCryptPasswordService;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author winiga
 */
@Stateless
public class UserDao {
    
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @PersistenceContext(unitName = "blogPU")
    private EntityManager em; 
    
    public void getEntityManager() {
        
        if(em == null) {
            EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("blogPU");//
            em = ENTITY_MANAGER_FACTORY.createEntityManager();
        }
    }
    
    public  boolean saveUser(User user) {
        
        EntityTransaction tx = em.getTransaction();
        Session session = em.unwrap(Session.class);
 
        try {
            
            tx.begin();
            
            session.save(user);
            
            tx.commit();
            
            return true;
            
            
        } catch(Exception ex) {
            if(tx != null) tx.rollback();
            
            log.error(ex.getMessage(), ex);
            
            return false;
        }
    }
    
    public  boolean saveUserWithCrypt(User user) {
        
        EntityTransaction tx = em.getTransaction();
        Session session = em.unwrap(Session.class);
        
        try {
            
            tx.begin();
            user.setPassword(new BCryptPasswordService().encryptPassword(user.getPassword()) );
            
            session.save(user);
            
            tx.commit();
            
            return true;
            
            
        } catch(IllegalArgumentException ex) {
            if(tx != null) tx.rollback();
            
            log.error(ex.getMessage(), ex);
            
            return false;
        }
    }
    
    public  boolean updateUserTokenByEmail(String email, String token) {
        
        EntityTransaction tx = em.getTransaction();
        Session session = em.unwrap(Session.class);
        
        try {
            
            tx.begin();
            //user.setPassword(new BCryptPasswordService().encryptPassword(user.getPassword()) );
            
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaUpdate<User> query = builder.createCriteriaUpdate(User.class);
            Root<User> root = query.from(User.class);
            
            query.set("token", token);
            query.where(builder.equal(root.get("email"), email));
            
            int q = session.createQuery(query).executeUpdate();
            
            tx.commit();
            
            return (q > 0) ;
        } catch(IllegalArgumentException ex) {
            if(tx != null) tx.rollback();
            
            log.error(ex.getMessage(), ex);
            
            return false;
        }
    }
    
    public User getUserById(Long id) {
        
        EntityTransaction tx = em.getTransaction();
        Session session = em.unwrap(Session.class);
        
        try{
            return session.byId(User.class).getReference(id);
        } catch(Exception ex) {
            if(tx != null) tx.rollback();
            
            log.error(ex.getMessage(), ex);
            
            return null;
        }
    }
    
    public User getUserByEmail(String email) {
        
        Session session = em.unwrap(Session.class);
        
        try{
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> query = builder.createQuery(User.class);
            Root<User> root = query.from(User.class);
            query.select(root).where(builder.equal(root.get("email"), email));
            TypedQuery<User> q=session.createQuery(query);
            
            return q.getSingleResult();
        } catch(HibernateException ex) {
            log.error(ex.getMessage(), ex);
            
            return null;
        }
    }
    
    public  String getUserToken(String email) {
        
        Session session = em.unwrap(Session.class);
        
        try{
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<String> query = builder.createQuery(String.class);
            
            Root<User> root = query.from(User.class);
            
            query.select(root.get("token")).where(builder.equal(root.get("email"), email));
            
            TypedQuery<String> q=session.createQuery(query);
            
            return q.getSingleResult();
        } catch(Exception ex) {
            log.error(ex.getMessage(), ex);
            
            return null;
        }
    }
    
    public  boolean deleteUser(User user) {
        
        EntityTransaction tx = em.getTransaction();
        Session session = em.unwrap(Session.class);
        
        try {
            
            tx.begin();
            
            session.delete(user);
            
            tx.commit();
            
            return true;
            
            
        } catch(Exception ex) {
            if(tx != null) tx.rollback();
            
            log.error(ex.getMessage(), ex);
            
            return false;
        }
    }
}
