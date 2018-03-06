 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.logic.domain.user.entity;

import com.smilan.logic.common.BasicEntity;
import com.smilan.logic.domain.security.CrudPermission;
import com.smilan.logic.domain.security.Realm;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import net.karneim.pojobuilder.GeneratePojoBuilder;
import org.apache.shiro.SecurityUtils;

/**
 *
 * @author Worph
 */
@Entity(name="user")
@GeneratePojoBuilder(withCopyMethod = true, intoPackage = "*.builder")
public class UserEntity implements Serializable, BasicEntity<UserEntity> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false, updatable = true)
    private String email;
    public final static String NAME_EMAIL="email";

    @Column(nullable = false, updatable = true, unique = true)
    private String login;
    public final static String NAME_LOGIN="login";

    @Column(nullable = false, updatable = true)
    private String password;//TODO security store hash of password
    public final static String NAME_PASSWORD="password";

    @Column(nullable = false, updatable = false)
    private String xmppPassword;    
    public final static String NAME_XMPPPASSWORD="xmpppassword";
    
    @Column(nullable = false, updatable = true)
    private boolean anonymous = true;//default true
    public final static String NAME_ANONYMOUS="anonymous";
    
    @Column(nullable = false, updatable = true)
    private String apiKey;
    public final static String NAME_APIKEY="apikey";
    
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles = new HashSet<>();
    public final static String NAME_ROLES="roles";
    
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> permissions = new HashSet<>();
    public final static String NAME_PERMISSION="permission";

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
    
    @Override
    public void copyNonNullValueTo(UserEntity to, CrudPermission permission) {
        UserEntity from = this;
        if (from.getEmail()!= null?!from.getEmail().equals(to.getEmail()):false) {
            permission.checkUpdatePermission(to.getId(), NAME_EMAIL);
            to.setEmail(from.getEmail());
        }
        if (from.getLogin() != null?!from.getLogin().equals(to.getLogin()):false) {
            permission.checkUpdatePermission(to.getId(), NAME_LOGIN);
            to.setLogin(from.getLogin());
        }
        if (from.getPassword()!= null?!from.getPassword().equals(to.getPassword()):false) {
                permission.checkUpdatePermission(to.getId(), NAME_PASSWORD);
                to.setPassword(from.getPassword());
        }
        if (from.getXmppPassword()!= null?!from.getXmppPassword().equals(to.getXmppPassword()):false) {
            permission.checkUpdatePermission(to.getId(), NAME_XMPPPASSWORD);
            to.setXmppPassword(from.getXmppPassword());
        }
        if (from.getApiKey()!= null?!from.getApiKey().equals(to.getApiKey()):false) {
            permission.checkUpdatePermission(to.getId(), NAME_APIKEY);
            to.setApiKey(from.getApiKey());
        }        
        if (from.getRoles()!= null?!from.getRoles().equals(to.getRoles()):false) {
            //permission.checkUpdatePermission(to.getId(), NAME_ROLES);
            SecurityUtils.getSubject().checkRole(Realm.ROLE_ADMIN);
            to.setRoles(from.getRoles());
        }
        if (from.getPermissions()!= null?!from.getPermissions().equals(to.getPermissions()):false) {
            //permission.checkUpdatePermission(to.getId(), NAME_PERMISSION);//cause stack overflow?
            SecurityUtils.getSubject().checkRole(Realm.ROLE_ADMIN);
            to.setPermissions(from.getPermissions());
        }
        if(from.isAnonymous()!=to.isAnonymous()){
            permission.checkUpdatePermission(to.getId(), NAME_ANONYMOUS);
            to.setAnonymous(from.isAnonymous());
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.id);
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
        final UserEntity other = (UserEntity) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "UserEntity{" + "id=" + id + ", email=" + email + ", login=" + login + ", password=" + password + ", XMPPpassword=" + xmppPassword + '}';
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getXmppPassword() {
        return xmppPassword;
    }

    public void setXmppPassword(String XMPPpassword) {
        this.xmppPassword = XMPPpassword;
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }


    
}
