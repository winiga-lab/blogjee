/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eii.jeeclassproject.jeeblog.dao;

import com.eii.jeeclassproject.jeeblog.controller.LoginController;
import com.eii.jeeclassproject.jeeblog.model.Post;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
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
    private EntityManager em;

    public boolean savePost(Post post) {

        Session session = em.unwrap(Session.class);

        try {
            session.save(post);
            return true;
        } catch (Exception ex) {

            log.error(ex.getMessage(), ex);

            return false;
        }
    }

    public boolean updatePost(Post post) {

        Session session = em.unwrap(Session.class);

        try {
            session.update(post);
            return true;
        } catch (Exception ex) {

            log.error(ex.getMessage(), ex);

            return false;
        }
    }

    public boolean incrementPostViewCount(int id) {

        Session session = em.unwrap(Session.class);

        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaUpdate<Post> query = builder.createCriteriaUpdate(Post.class);
            Root<Post> root = query.from(Post.class);
            query.set(root.<Long>get("views"), builder.sum(root.get("views"), Long.valueOf(1)));
            query.where(builder.equal(root.get("id"), id));

            int q = session.createQuery(query).executeUpdate();

            return (q > 0);
        } catch (IllegalArgumentException ex) {
            log.error(ex.getMessage(), ex);

            return false;
        }
    }

    public boolean incrementPostViewCount(String title) {

        Session session = em.unwrap(Session.class);

        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaUpdate<Post> query = builder.createCriteriaUpdate(Post.class);
            Root<Post> root = query.from(Post.class);
            query.set(root.<Long>get("views"), builder.sum(root.get("views"), Long.valueOf(1)));
            query.where(builder.equal(root.get("title"), title));

            int q = session.createQuery(query).executeUpdate();

            return (q > 0);
        } catch (IllegalArgumentException ex) {
            log.error(ex.getMessage(), ex);

            return false;
        }
    }

    /**
     * sum of all views on posts
     *
     * @return
     */
    public long getAllTotalViewCount() {

        Session session = em.unwrap(Session.class);

        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<BigDecimal> query = builder.createQuery(BigDecimal.class);
            Root<Post> root = query.from(Post.class);

            query.select(builder.sum(root.get("views").as(BigDecimal.class)));

            TypedQuery<BigDecimal> q = session.createQuery(query);

            return q.getSingleResult().longValue();

        } catch (IllegalArgumentException ex) {
            log.error(ex.getMessage(), ex);

            return 0;
        }
    }

    public int countPost() {

        Session session = em.unwrap(Session.class);

        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Long> query = builder.createQuery(Long.class);
            Root<Post> root = query.from(Post.class);

            query.select(builder.count(root)).where(builder.equal(root.get("draft"), 0));

            TypedQuery<Long> q = session.createQuery(query);

            return q.getSingleResult().intValue();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);

            return 0;
        }
    }

    public int totalPage(int resultPerPage) {

        double resultCount = countPost();

        if (Math.ceil(resultCount / resultPerPage) != 0) {
            return (int) Math.ceil(resultCount / resultPerPage);
        }
        return 1;
    }

    /**
     * Review
     *
     * @return
     */
    public List<Post> getPosts() {

        Session session = em.unwrap(Session.class);

        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Post> query = builder.createQuery(Post.class);
            Root<Post> root = query.from(Post.class);

            query.select(root);

            TypedQuery<Post> q = session.createQuery(query);

            return q.getResultList();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);

            return null;
        }
    }

    /**
     * Review
     *
     * @param page
     * @param resultPerPage
     * @return
     */
    public List<Post> getPaginatePosts(int page, int resultPerPage) {

        Session session = em.unwrap(Session.class);

        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Post> query = builder.createQuery(Post.class);
            Root<Post> root = query.from(Post.class);

            query.select(root);

            TypedQuery<Post> q = session.createQuery(query);

            q.setFirstResult((page - 1) * resultPerPage);
            q.setMaxResults(resultPerPage);
            return q.getResultList();
        } catch (Exception ex) {

            log.error(ex.getMessage(), ex);

            return null;
        }
    }

    public List<Post> getPaginatePostsWithoutDetails(int page, int resultPerPage) {

        Session session = em.unwrap(Session.class);

        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Post> query = builder.createQuery(Post.class);
            Root<Post> root = query.from(Post.class);

            query.multiselect(root.get("id"), root.get("title"), root.get("resume"), root.get("views"), root.get("datePost"), root.get("user"));

            TypedQuery<Post> q = session.createQuery(query);

            q.setFirstResult((page - 1) * resultPerPage);
            q.setMaxResults(resultPerPage);
            return q.getResultList();
        } catch (Exception ex) {

            log.error(ex.getMessage(), ex);

            return null;
        }
    }

    public List<Post> getPaginatePublished(int page, int resultPerPage) {

        Session session = em.unwrap(Session.class);

        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Post> query = builder.createQuery(Post.class);
            Root<Post> root = query.from(Post.class);

            query.multiselect(root.get("id"), root.get("title"), root.get("resume"), root.get("views"), root.get("datePost"), root.get("user"))
                    .where(builder.equal(root.get("draft"), 0));

            TypedQuery<Post> q = session.createQuery(query);

            q.setFirstResult((page - 1) * resultPerPage);
            q.setMaxResults(resultPerPage);
            return q.getResultList();
        } catch (Exception ex) {

            log.error(ex.getMessage(), ex);

            return null;
        }
    }

    /**
     * Review
     *
     * @param page
     * @param resultPerPage
     * @return
     */
    public List<Post> getMixedPaginatePosts(int page, int resultPerPage) {

        List<Post> mixedResult = getPaginatePosts(page, resultPerPage);

        Collections.shuffle(mixedResult);

        return mixedResult;
    }

    public List<Post> getMixedPaginatePostsWithoutDetails(int page, int resultPerPage) {

        List<Post> mixedResult = getPaginatePosts(page, resultPerPage);

        Collections.shuffle(mixedResult);

        return mixedResult;
    }

    public Post getPostById(int id) {

        Session session = em.unwrap(Session.class);

        try {
            return session.byId(Post.class).getReference(id);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);

            return null;
        }
    }

    public Post getPostByTitle(String title) {

        Session session = em.unwrap(Session.class);

        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Post> query = builder.createQuery(Post.class);
            Root<Post> root = query.from(Post.class);

            query.select(root).where(builder.equal(root.get("title"), title));

            TypedQuery<Post> q = session.createQuery(query);

            return q.getSingleResult();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);

            return null;
        }
    }

    public HashMap<Date,Long> getCreatedByDate() {

        Session session = em.unwrap(Session.class);
        HashMap<Date, Long> statCreationPost = new HashMap<>();
        try {
            Query q = session.createNamedQuery("Post.findNumberPostBy");
            List<Object[]> results = q.getResultList();
            results.forEach(result -> {
                statCreationPost.put((Date)result[0], (Long)result[1]);
            });
            return statCreationPost;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            
            return null;
        }
    }

    public boolean deletePost(Post post) {

        Session session = em.unwrap(Session.class);

        try {

            session.delete(post);

            return true;
        } catch (Exception ex) {

            log.error(ex.getMessage(), ex);

            return false;
        }
    }

}
