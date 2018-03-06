package com.smilan.logic.domain.security.configuration;

import com.smilan.api.common.support.PropertyHelper;
import com.smilan.logic.domain.announce.AnnounceLogic;
import com.smilan.logic.domain.announce.configuration.AnnounceLogicConfiguration;
import com.smilan.logic.domain.profile.ProfileLogic;
import com.smilan.logic.domain.profile.configuration.ProfileLogicConfiguration;
import com.smilan.logic.domain.security.Realm;
import static com.smilan.logic.domain.security.Realm.PPT_ADMIN_PASSWORD;
import static com.smilan.logic.domain.security.Realm.PPT_ADMIN_USERNAME;
import com.smilan.logic.domain.user.UserLogic;
import com.smilan.logic.domain.user.configuration.UserLogicConfiguration;
import java.util.Properties;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
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
    ProfileLogicConfiguration.class,
    AnnounceLogicConfiguration.class,
    UserLogicConfiguration.class
})
public class RealmConfiguration {
    
    @Bean
    Realm customRealm(ProfileLogic profileLogic,UserLogic userLogic,AnnounceLogic announceLogic,Environment environment) {
        Properties property = new Properties();
        PropertyHelper.putIfPresent(property, environment, PPT_ADMIN_USERNAME);
        PropertyHelper.putIfPresent(property, environment, PPT_ADMIN_PASSWORD);
        return new Realm(profileLogic,userLogic, announceLogic, property);
    }
    
    @Bean 
    SecurityManager administartionSecurityManager(Realm customRealm){
        final DefaultSecurityManager defaultWebSecurityManager = new DefaultSecurityManager(customRealm);
        return defaultWebSecurityManager;
    }
        
}
