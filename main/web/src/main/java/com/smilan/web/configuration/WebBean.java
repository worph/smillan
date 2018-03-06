/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.web.configuration;

import com.smilan.api.common.dto.Contexte;
import com.smilan.api.common.dto.builder.ContexteBuilder;
import com.smilan.support.property.PropertiesConfigurationPropertySource;
import com.smilan.web.configuration.builder.ContextePopulatorInterceptorBuilder;
import java.io.IOException;
import java.util.UUID;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.io.Resource;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * @author Pierre-Henri
 */
@Configuration
public class WebBean {
    @Bean
    public ContextePopulatorInterceptor contextePopulatorInterceptor(Contexte contexte) {
        return new ContextePopulatorInterceptorBuilder()
                .withContexte(contexte)
                .build();
    }

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public Contexte contexte() {
        return new ContexteBuilder()
                .withIdentifiantCorrelation(UUID.randomUUID().toString())
                .withApplication("smilan-web")
                .build();
    }

    @Bean
    public PropertiesConfigurationPropertySource techniquesPropertiesConfigurationPropertySource(@Value("classpath:com/smilan/conf/main.properties") Resource resource) {
        try {
            PropertiesConfiguration propertiesConfiguration = new PropertiesConfiguration(resource.getURL());
            propertiesConfiguration.setReloadingStrategy(new FileChangedReloadingStrategy());
            return new PropertiesConfigurationPropertySource("techniques", propertiesConfiguration);
        } catch (ConfigurationException | IOException exception) {
            throw new Error(exception);
        }
    }
    
}
