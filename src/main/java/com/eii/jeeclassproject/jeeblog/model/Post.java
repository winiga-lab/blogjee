/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eii.jeeclassproject.jeeblog.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author winiga
 */
@Entity
@Table(name = "posts")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Post.findAll", query = "SELECT p FROM Post p"),
    @NamedQuery(name = "Post.findById", query = "SELECT p FROM Post p WHERE p.id = :id"),
    @NamedQuery(name = "Post.findByDatePost", query = "SELECT p FROM Post p WHERE p.datePost = :datePost"),
    @NamedQuery(name = "Post.findByTitle", query = "SELECT p FROM Post p WHERE p.title = :title"),
    @NamedQuery(name = "Post.findByDetails", query = "SELECT p FROM Post p WHERE p.details = :details")})
public class Post implements Serializable {

    @Basic(optional = false)
    @NotNull()
    @Size(min = 1, max = 45)
    @Column(name = "title")
    private String title;
    @Basic(optional = false)
    @NotNull()
    @Lob()
    @Size(min = 1, max = 2147483647)
    @Column(name = "details")
    private String details;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "resume")
    private String resume;
    @Basic(optional = false)
    @NotNull
    @Column(name = "views")
    private long views;
    @Basic(optional = false)
    @NotNull
    @Column(name = "draft")
    private short draft;
    @Basic(optional = false)
    @NotNull
    @Column(name = "isEdited")
    private short isEdited;
    @Column(name = "date_edition")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEdition;
    @Basic(optional = false)
    @NotNull
    @Column(name = "isDelete")
    private short isDelete;
    @ManyToOne @JoinColumn(name="owner_id", nullable = false )
    private User user;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "date_post")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datePost;

    public Post() {
    }

    public Post(Integer id,String title, String resume, Long views, Date datePost, User user) {
        this.title = title;
        this.resume = resume;
        this.views = views;
        this.user = user;
        this.id = id;
        this.datePost = datePost;
    }
    
    
    
    @PrePersist
    public void defaultDatePost() {
        
        if(this.datePost == null) {
            this.datePost = new Date(System.currentTimeMillis());
        }
    }

    public Post(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDatePost() {
        return datePost;
    }

    public void setDatePost(Date datePost) {
        this.datePost = datePost;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Post)) {
            return false;
        }
        Post other = (Post) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.eii.jeeclassproject.jeeblog.model.Post[ id=" + id + " ]";
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }

    public short getDraft() {
        return draft;
    }

    public void setDraft(short draft) {
        this.draft = draft;
    }

    public short getIsEdited() {
        return isEdited;
    }

    public void setIsEdited(short isEdited) {
        this.isEdited = isEdited;
    }

    public Date getDateEdition() {
        return dateEdition;
    }

    public void setDateEdition(Date dateEdition) {
        this.dateEdition = dateEdition;
    }

    public short getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(short isDelete) {
        this.isDelete = isDelete;
    }
    
    
}
