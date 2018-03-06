/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.logic.domain.category.entity;

import com.smilan.logic.common.BasicEntity;
import com.smilan.logic.domain.security.CrudPermission;
import com.smilan.logic.domain.announce.entity.AnnounceEntity;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.UniqueConstraint;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 *
 * @author Worph
 */
@Entity(name="category")
@GeneratePojoBuilder(withCopyMethod = true)
public class CategoryEntity implements Serializable, BasicEntity<CategoryEntity> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idCategory" ,nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false, updatable = false,unique=true)
    private String value;
    
    @Column(nullable = true, updatable = true)
    private String password;
    
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "categories", cascade = CascadeType.ALL)
    private Set<AnnounceEntity> announces = new HashSet<AnnounceEntity>(0);
    
    public Set<AnnounceEntity> getAnnounces() {
        return announces;
    }
    
    public void setAnnounces(Set<AnnounceEntity> announces) {
        this.announces = announces;
    }
        
    @Override
    public void copyNonNullValueTo(CategoryEntity to, CrudPermission permission) {
        CategoryEntity from = this;
        if (from.getValue() != null) {
            if(!to.getValue().equals(from.getValue())){
                throw new UnsupportedOperationException("field value is not updatable");
            }
        }
        if (from.getPassword()!= null) {
            permission.checkUpdatePermission(to.getId(), "password");
            to.setPassword(from.getPassword());
        }
    }

    @Override
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.id);
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
        final CategoryEntity other = (CategoryEntity) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CategoryEntity{" + "id=" + id + ", value=" + value + '}';
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
