/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.test.common.support.configuration;

import com.smilan.api.common.dto.Contexte;
import com.smilan.logic.common.configuration.CommunLogicConfiguration;
import com.smilan.logic.domain.security.Realm;
import com.smilan.logic.domain.security.configuration.RealmConfiguration;
import com.smilan.test.common.data.CommunDataTest;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 *
 * @author pierr
 */
@Configuration
@Import({RealmConfiguration.class,CommunLogicConfiguration.class})
public class CommonConfiguration {
    
    @Bean
    public Contexte contexte(Realm customRealm) {
        SecurityUtils.setSecurityManager(new DefaultSecurityManager(customRealm));
        final Subject subject = SecurityUtils.getSubject();
        subject.login(new UsernamePasswordToken("admin","admin"));
        return new CommunDataTest().contexteBuilder().build();
    }
    
}
