/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.process.domain.chat.configuration;

import com.smilan.api.common.support.PropertyHelper;
import com.smilan.api.domain.chat.ChatManager;
import com.smilan.process.domain.chat.DefaultChatManager;
import java.util.Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 *
 * @author pierr
 */
@Configuration
public class ChatConfiguration {
        
    @Bean
    public ChatManager chatManager(Environment environment) {
        Properties property = new Properties();
        PropertyHelper.putIfPresent(property, environment, DefaultChatManager.PPT_HOSTNAME_XML_RPC);
        PropertyHelper.putIfPresent(property, environment, DefaultChatManager.PPT_PREFIX);
        PropertyHelper.putIfPresent(property, environment, DefaultChatManager.PPT_ADMIN_NAME);
        PropertyHelper.putIfPresent(property, environment, DefaultChatManager.PPT_ADMIN_PASSWORD);
        return new DefaultChatManager(property); 
    }
}
