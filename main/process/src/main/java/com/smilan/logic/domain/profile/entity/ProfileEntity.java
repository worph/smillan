/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.logic.domain.profile.entity;

import com.smilan.logic.common.BasicEntity;
import com.smilan.logic.domain.security.CrudPermission;
import com.smilan.logic.domain.media.entity.MediaEntity;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 *
 * @author Worph
 */
@Entity(name="profile")
@GeneratePojoBuilder(withCopyMethod = true, intoPackage = "*.builder")
public class ProfileEntity implements Serializable, BasicEntity<ProfileEntity> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idProfile", nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false, updatable = false)
    private Long userId;
    public static final String NAME_USERID = "userid";

    @Column(nullable = false, updatable = true)
    private String profileName;
    public static final String NAME_PROFILENAME = "profilename";

    @Column(nullable = false, updatable = false)
    private String xmppId;    
    public static final String NAME_XMPPID = "xmppid";
    
    @Column(nullable = false, updatable = true, columnDefinition = "TEXT")
    private String description;
    public static final String NAME_DESCRIPTION = "description";

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "profileavatar_media", joinColumns = {
                    @JoinColumn(name = "idProfile", nullable = false, updatable = true) },
                    inverseJoinColumns = { 
                        @JoinColumn(name = "idMedia", nullable = false, updatable = true) })
    private MediaEntity avatar;
    public static final String NAME_AVATAR= "avatar";
    
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "profilebackground_media", joinColumns = {
                    @JoinColumn(name = "idProfile", nullable = false, updatable = true) },
                    inverseJoinColumns = { 
                        @JoinColumn(name = "idMedia", nullable = false, updatable = true) })
    private MediaEntity background;
    public static final String NAME_BACKGROUND= "background";
        
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created", nullable = false, updatable = false)
    private Date created;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated", nullable = false)
    private Date updated;

    @PrePersist
    protected void onCreate() {
        updated = created = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updated = new Date();
    }
    
    public Date getCreated() {
        return created;
    }

    public Date getUpdated() {
        return updated;
    }

    @Override
    public void copyNonNullValueTo(ProfileEntity to, CrudPermission permission) {
        //to = persisted entity
        ProfileEntity from = this;
        if (from.getUserId() != null) {
            permission.checkUpdatePermission(to.getId(), NAME_USERID);
            to.setUserId(from.getUserId());
        }
        if (from.getProfileName() != null) {
            permission.checkUpdatePermission(to.getId(), NAME_PROFILENAME);
            to.setProfileName(from.getProfileName());
        }
        if (from.getXmppId() != null) {
            permission.checkUpdatePermission(to.getId(), NAME_XMPPID);
            to.setXmppId(from.getXmppId());
        }
        if (from.getDescription() != null) {
            permission.checkUpdatePermission(to.getId(), NAME_DESCRIPTION);
            to.setDescription(from.getDescription());
        }
        if (from.getAvatar() != null) {
            if(from.getAvatar().getId()==null){
                permission.checkUpdatePermission(to.getId(), NAME_AVATAR);
                //this is a new media to be persisted
                to.setAvatar(from.getAvatar());
            }else{
                //nothing to do
            }
        }else{
            throw new Error();
        }
        if (from.getBackground() != null) {
            if(from.getBackground().getId()==null){
                permission.checkUpdatePermission(to.getId(), NAME_BACKGROUND);
                //this is a new media to be persisted
                to.setBackground(from.getBackground());
            }else{
                //nothing to do
            }
        }else{
            throw new Error();
        }
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getXmppId() {
        return xmppId;
    }

    public void setXmppId(String xmppId) {
        this.xmppId = xmppId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MediaEntity getAvatar() {
        return avatar;
    }

    public void setAvatar(MediaEntity avatar) {
        this.avatar = avatar;
    }

    public MediaEntity getBackground() {
        return background;
    }

    public void setBackground(MediaEntity background) {
        this.background = background;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }



}
