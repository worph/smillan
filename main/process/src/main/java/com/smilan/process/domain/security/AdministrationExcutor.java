/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.process.domain.security;

import com.google.common.collect.Lists;
import com.smilan.logic.domain.security.Realm;
import static com.smilan.logic.domain.security.Realm.PPT_ADMIN_PASSWORD;
import static com.smilan.logic.domain.security.Realm.PPT_ADMIN_USERNAME;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.Callable;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;

/**
 *
 * @author pierr
 */
public class AdministrationExcutor {
    
    private final SecurityManager securityManager;
    private final AuthenticationToken authenticationToken;
    private final Realm customRealm;

    public AdministrationExcutor(SecurityManager securityManager, Properties property,Realm customRealm) {
        this.securityManager = securityManager;
        String adminUsername = property.getProperty(PPT_ADMIN_USERNAME, "admin");
        String adminPassword = property.getProperty(PPT_ADMIN_PASSWORD, "admin");
        this.authenticationToken = new UsernamePasswordToken(adminUsername, adminPassword);
        this.customRealm = customRealm;
    }

    public void run(Runnable runnable) {
        Subject subject;
        try {
            SecurityUtils.getSecurityManager();
            subject = SecurityUtils.getSubject();
        } catch (org.apache.shiro.UnavailableSecurityManagerException ex) {
            org.apache.shiro.util.ThreadContext.bind(securityManager);
            subject = new Subject.Builder(securityManager).buildSubject();
        }
        boolean runas = false;
        if (subject.isAuthenticated()) {
            runas = true;
        }
        if (runas) {
            final SimplePrincipalCollection simplePrincipalCollection = new SimplePrincipalCollection();
            simplePrincipalCollection.add(Realm.ROLE_ADMIN, customRealm.getName());
            subject.runAs(simplePrincipalCollection);
        }else{
            subject.login(authenticationToken);
        }
        System.out.println("execute as : "+subject.getPrincipal());
        subject.execute(runnable);
        if (runas) {
            subject.releaseRunAs();
        }else{
            subject.logout();//go back to not authenticated
        }
    }

    public AuthenticationToken getAdminAuthToken() {
        return authenticationToken;
    }
    
    
}
