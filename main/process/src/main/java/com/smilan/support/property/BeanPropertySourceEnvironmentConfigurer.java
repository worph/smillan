package com.smilan.support.property;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

/**
 * @author Thomas
 *
 */
public class BeanPropertySourceEnvironmentConfigurer implements InitializingBean {

    private final ConfigurableEnvironment configurableEnvironment;

    private final List<PropertySource<?>> propertySources;

    private final List<String>            propertySourceNames = new ArrayList<>();

    /**
     * @param configurableEnvironment
     * @param propertySources
     */
    public BeanPropertySourceEnvironmentConfigurer(ConfigurableEnvironment configurableEnvironment, List<PropertySource<?>> propertySources) {
        super();
        this.configurableEnvironment = configurableEnvironment;
        this.propertySources = propertySources;
    }

    /**
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        for (PropertySource<?> propertySource : this.propertySources) {
            this.addPropertySource(propertySource);
        }
    }

    private void addPropertySource(PropertySource<?> propertySource) {
        String name = propertySource.getName();
        MutablePropertySources mutablePropertySources = this.configurableEnvironment.getPropertySources();
        if (mutablePropertySources.contains(name) && this.propertySourceNames.contains(name)) {
            // We've already added a version, we need to extend it
            PropertySource<?> existing = mutablePropertySources.get(name);
            if (existing instanceof CompositePropertySource) {
                ((CompositePropertySource) existing).addFirstPropertySource(propertySource);
            }
            else {
                CompositePropertySource composite = new CompositePropertySource(name);
                composite.addPropertySource(propertySource);
                composite.addPropertySource(existing);
                mutablePropertySources.replace(name, composite);
            }
        }
        else {
            if (this.propertySourceNames.isEmpty()) {
                mutablePropertySources.addLast(propertySource);
            }
            else {
                String firstProcessed = this.propertySourceNames.get(this.propertySourceNames.size() - 1);
                mutablePropertySources.addBefore(firstProcessed, propertySource);
            }
        }
        this.propertySourceNames.add(name);
    }
}
