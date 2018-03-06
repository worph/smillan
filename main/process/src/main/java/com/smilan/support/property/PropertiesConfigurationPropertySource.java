package com.smilan.support.property;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.core.env.PropertySource;

/**
 * @author Thomas
 *
 */
public class PropertiesConfigurationPropertySource extends PropertySource<PropertiesConfiguration> {

    /**
     * @param name
     * @param source
     */
    public PropertiesConfigurationPropertySource(String name, PropertiesConfiguration source) {
        super(name, source);
    }

    /**
     * @param name
     */
    public PropertiesConfigurationPropertySource(String name) {
        super(name);
    }

    /**
     * @see org.springframework.core.env.PropertySource#getProperty(java.lang.String)
     */
    @Override
    public Object getProperty(String name) {
        return this.source.getProperty(name);
    }
}
