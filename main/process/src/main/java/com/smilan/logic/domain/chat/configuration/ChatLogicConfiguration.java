/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.logic.domain.chat.configuration;

import com.smilan.api.common.support.PropertyHelper;
import com.smilan.logic.domain.chat.XMPPService;
import java.util.Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 *
 * @author pierr
 */
@Configuration
public class ChatLogicConfiguration {
        
    @Bean
    public XMPPService xmppService(Environment environment) {
        Properties property = new Properties();
        PropertyHelper.putIfPresent(property, environment, XMPPService.PPT_ADMIN_NAME);
        PropertyHelper.putIfPresent(property, environment, XMPPService.PPT_ADMIN_PASSWORD);
        PropertyHelper.putIfPresent(property, environment, XMPPService.PPT_HOSTNAME);
        PropertyHelper.putIfPresent(property, environment, XMPPService.PPT_HOSTNAME_HTTP_BIND);
        PropertyHelper.putIfPresent(property, environment, XMPPService.PPT_HOSTNAME_XML_RPC);
        PropertyHelper.putIfPresent(property, environment, XMPPService.PPT_PREFIX);
        PropertyHelper.putIfPresent(property, environment, XMPPService.PPT_SIMULATE_BEHAVIOUR);
        return new XMPPService(property); 
    }
}
