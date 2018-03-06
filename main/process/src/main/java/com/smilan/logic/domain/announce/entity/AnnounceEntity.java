/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.logic.domain.announce.entity;

import com.smilan.api.domain.announce.GeoLocation;
import com.smilan.api.domain.announce.builder.GeoLocationBuilder;
import com.smilan.logic.common.BasicEntity;
import com.smilan.logic.domain.security.CrudPermission;
import com.smilan.logic.domain.category.entity.CategoryEntity;
import com.smilan.logic.domain.media.entity.MediaEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 *
 * @author Worph
 */
@Entity(name="announce")
@GeneratePojoBuilder(withCopyMethod = true, intoPackage = "*.builder")
public class AnnounceEntity implements Serializable, BasicEntity<AnnounceEntity> {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column(name="idAnnounce",nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false, updatable = false)
    private Long profileId;

    @Column(nullable = false, updatable = true)
    private String title;
    
    @Column(nullable = false, updatable = true)
    private String type;
    
    @Column(nullable = true, updatable = true)
    private String chatId;

    @Column(nullable = true, updatable = true, columnDefinition = "TEXT")
    private String text;
        
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "announce_media", joinColumns = {
                    @JoinColumn(name = "idAnnounce", nullable = false, updatable = false) },
                    inverseJoinColumns = { 
                        @JoinColumn(name = "idMedia", nullable = false, updatable = false) })
    private List<MediaEntity> media;
    
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "announce_category", joinColumns = {
                    @JoinColumn(name = "idAnnounce", nullable = false, updatable = false) },
                    inverseJoinColumns = { 
                        @JoinColumn(name = "idCategory", nullable = false, updatable = false) })
    private List<CategoryEntity> categories; //this field name is used by maped_by property
    
    @Embedded
    private GeoLocation location;//TODO make a list of GeoLoc adapted to geodist search
    
    public static final String latLocationPath = "announceEntity.location.lat";
    public static final String lonLocationPath = "announceEntity.location.lon";
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created", nullable = false)
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    @Override
    public void copyNonNullValueTo(AnnounceEntity to, CrudPermission permission) {
        //field id, created and updated are not copied since they are auto updated.
        AnnounceEntity from = this;
        if (from.getText() != null) {
            permission.checkUpdatePermission(to.getId(), "text");
            to.setText(from.getText());
        }
        if (from.getProfileId() != null) {
            permission.checkUpdatePermission(to.getId(), "profileId");
            to.setProfileId(from.getProfileId());
        }
        if (from.getType() != null) {
            permission.checkUpdatePermission(to.getId(), "type");
            to.setTitle(from.getType());
        }
        if (from.getChatId()!= null) {
            permission.checkUpdatePermission(to.getId(), "chatId");
            to.setChatId(from.getChatId());
        }
        if (from.getTitle() != null) {
            permission.checkUpdatePermission(to.getId(), "title");
            to.setTitle(from.getTitle());
        }
        if (from.getCategories() != null) {
            permission.checkUpdatePermission(to.getId(), "categories");
            to.setCategories(new ArrayList<>(from.getCategories()));
        }
        if (from.getMedia() != null) {
            permission.checkUpdatePermission(to.getId(), "media");
            to.setMedia(new ArrayList<>(from.getMedia()));
        }
        if (from.getLocation() != null) {
            permission.checkUpdatePermission(to.getId(), "location");
            to.setLocation(new GeoLocationBuilder().copy(from.getLocation()).build());
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AnnounceEntity other = (AnnounceEntity) obj;
        return Objects.equals(this.id, other.id);
    }

    public GeoLocation getLocation() {
        return location;
    }

    public void setLocation(GeoLocation location) {
        this.location = location;
    }

    public List<MediaEntity> getMedia() {
        return media;
    }

    public void setMedia(List<MediaEntity> media) {
        this.media = media;
    }

    public List<CategoryEntity> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryEntity> categories) {
        this.categories = categories;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }
    

}
