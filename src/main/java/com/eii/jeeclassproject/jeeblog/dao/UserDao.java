/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eii.jeeclassproject.jeeblog.dao;

import com.eii.jeeclassproject.jeeblog.controller.LoginController;
import com.eii.jeeclassproject.jeeblog.model.User;
import com.eii.jeeclassproject.jeeblog.security.BCryptPasswordService;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author winiga
 */
public class UserDao {
    
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    
    private EntityManager em; 
    
    private EntityTransaction tx;
    
    private Session session;

    /**
     * 
     */
    public UserDao() {
        EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("blogPU");//
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        
        tx =  em.getTransaction(); 
        
        session = em.unwrap(Session.class);
    }
    
    public boolean saveUser(User user) {
        
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
    
    public boolean saveUserWithCrypt(User user) {
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
    
    public User getUserById(Long id) {
        
        try{
            return session.byId(User.class).getReference(id);
        } catch(Exception ex) {
            if(tx != null) tx.rollback();
            
            log.error(ex.getMessage(), ex);
            
            return null;
        }
    }
    
    public User getUserByEmail(String email) {
        try{
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> query = builder.createQuery(User.class);
            Root<User> root = query.from(User.class);
            query.select(root).where(builder.equal(root.get("email"), email));
            TypedQuery<User> q=session.createQuery(query);
            
            return q.getSingleResult();
        } catch(HibernateException ex) {
            if(tx != null) tx.rollback();
            
            log.error(ex.getMessage(), ex);
            
            return null;
        }
    }
    
    public boolean deleteUser(User user) {
        
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
