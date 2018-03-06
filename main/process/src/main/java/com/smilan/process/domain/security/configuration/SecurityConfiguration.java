/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.process.domain.security.configuration;

import com.smilan.api.common.support.PropertyHelper;
import com.smilan.logic.domain.security.Realm;
import static com.smilan.logic.domain.security.Realm.PPT_ADMIN_PASSWORD;
import static com.smilan.logic.domain.security.Realm.PPT_ADMIN_USERNAME;
import com.smilan.logic.domain.security.configuration.RealmConfiguration;
import com.smilan.logic.domain.user.UserLogic;
import com.smilan.logic.domain.user.configuration.UserLogicConfiguration;
import com.smilan.process.domain.security.AdministrationExcutor;
import com.smilan.process.domain.security.RESTApiAuthenticator;
import java.util.Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

/**
 *
 * @author pierr
 */
@Configuration
@Import({
    UserLogicConfiguration.class,
    RealmConfiguration.class
})
public class SecurityConfiguration {
    @Bean
    RESTApiAuthenticator restApiAuthenticator(UserLogic userLogic){
        return new RESTApiAuthenticator(userLogic);
    }
    
    @Bean 
    AdministrationExcutor administrationExcutor(org.apache.shiro.mgt.SecurityManager administartionSecurityManager, Environment environment,Realm customRealm){
        Properties property = new Properties();
        PropertyHelper.putIfPresent(property, environment, PPT_ADMIN_USERNAME);
        PropertyHelper.putIfPresent(property, environment, PPT_ADMIN_PASSWORD);
        return new AdministrationExcutor(administartionSecurityManager, property,customRealm);
    }
}
