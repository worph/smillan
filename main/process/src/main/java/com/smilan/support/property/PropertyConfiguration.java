package com.smilan.support.property;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.security.crypto.encrypt.Encryptors;

import com.smilan.support.property.crypto.EncryptablePropertySourcesConfigurer;

/**
 * @author Thomas
 *
 */
@Configuration
public class PropertyConfiguration {

    @Bean
    public BeanPropertySourceEnvironmentConfigurer beanPropertySourceEnvironmentConfigurer(ConfigurableEnvironment configurableEnvironment,
            Optional<List<PropertySource<?>>> optionalPropertySources) {
        return new BeanPropertySourceEnvironmentConfigurer(configurableEnvironment, optionalPropertySources.orElse(new ArrayList<PropertySource<?>>()));
    }

    @Bean
    @DependsOn("beanPropertySourceEnvironmentConfigurer")
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    @DependsOn({ "beanPropertySourceEnvironmentConfigurer", "propertySourcesPlaceholderConfigurer" })
    @Conditional({ SecurityPropertyCondition.class })
    public EncryptablePropertySourcesConfigurer encryptablePropertySourcesConfigurer(ConfigurableEnvironment configurableEnvironment,
            @Value("${securite.password}") String password, @Value("${securite.salt}") String salt) {
        return new EncryptablePropertySourcesConfigurer(Encryptors.queryableText(password, salt), configurableEnvironment);
    }

    /**
     * @author Thomas
     *
     */
    public static class SecurityPropertyCondition implements Condition {

        /** @see org.springframework.context.annotation.Condition#matches(org.springframework.context.annotation.ConditionContext, org.springframework.core.type.AnnotatedTypeMetadata) */
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            if (context.getEnvironment().containsProperty("securite.password") && context.getEnvironment().containsProperty("securite.salt")) {
                return true;
            } else {
                return false;
            }
        }

    }
}
