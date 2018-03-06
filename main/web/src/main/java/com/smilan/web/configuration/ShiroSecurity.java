/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.web.configuration;

import com.smilan.logic.domain.security.Realm;
import com.smilan.logic.domain.security.configuration.RealmConfiguration;
import java.util.HashMap;
import java.util.Map;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.SubjectThreadState;
import org.apache.shiro.util.ThreadState;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.subject.WebSubject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Import;

/**
 *
 * @author pierr
 */
@Configuration
@Import(RealmConfiguration.class)
public class ShiroSecurity {
    
    @Bean
    ShiroFilterFactoryBean shiroFilter(org.apache.shiro.mgt.SecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        String supplementalOption = "";//ssl on prod server
        supplementalOption = supplementalOption.equals("")?"":", "+supplementalOption;
        Map<String,String> def= new HashMap<>();
        def.put("/", "anon");
        def.put("/resources/", "authcBasic, roles[admin]"+supplementalOption);
        def.put("/resources/src/**", "authcBasic, roles[admin]"+supplementalOption);
        def.put("/resources/admin/**", "authcBasic, roles[admin]"+supplementalOption);
        def.put("/resources/password/**", "anon");
        def.put("/resources/js/**", "anon");
        def.put("/resources/assets/**", "anon");
        def.put("/api/**", "anon"+supplementalOption);
        shiroFilterFactoryBean.setFilterChainDefinitionMap(def);
        return shiroFilterFactoryBean;
    }

    @Bean
    org.apache.shiro.web.mgt.DefaultWebSecurityManager securityManager(Realm customRealm){
        final DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager(customRealm);
        return defaultWebSecurityManager;
    }

    @Bean
    org.apache.shiro.spring.LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator(org.apache.shiro.spring.LifecycleBeanPostProcessor lifecycleBeanPostProcessor) {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        return advisorAutoProxyCreator;
    }

    @Bean
    AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(org.apache.shiro.mgt.SecurityManager securityManager) {
        final AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

}
