package com.smilan.support.property.crypto;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.security.crypto.encrypt.TextEncryptor;

import com.smilan.support.property.crypto.wrapper.EncryptableEnumerablePropertySourceWrapper;
import com.smilan.support.property.crypto.wrapper.EncryptableMapPropertySourceWrapper;
import com.smilan.support.property.crypto.wrapper.EncryptablePropertySourceWrapper;

/**
 * @author Thomas
 *
 */
public class EncryptablePropertySourcesConfigurer implements InitializingBean {

    private static final Logger     LOGGER = LoggerFactory.getLogger(EncryptablePropertySourcesConfigurer.class);

    private TextEncryptor           textEncryptor;

    private ConfigurableEnvironment configurableEnvironment;

    /**
     * @param textEncryptor
     * @param configurableEnvironment
     */
    public EncryptablePropertySourcesConfigurer(TextEncryptor textEncryptor, ConfigurableEnvironment configurableEnvironment) {
        super();
        this.textEncryptor = textEncryptor;
        this.configurableEnvironment = configurableEnvironment;
    }

    /**
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        EncryptablePropertySourcesConfigurer.LOGGER.info("postProcessBeanFactory(): Post-processing PropertySource instances");
        MutablePropertySources propertySources = this.configurableEnvironment.getPropertySources();
        StreamSupport.stream(propertySources.spliterator(), false).filter(propertySource -> !(propertySource instanceof EncryptablePropertySource))
                .map(this::makeEncryptable).collect(Collectors.toList())
                .forEach(propertySource -> propertySources.replace(propertySource.getName(), propertySource));
    }

    private PropertySource<?> makeEncryptable(PropertySource<?> propertySource) {
        PropertySource<?> encryptablePropertySource = this.instantiatePropertySource(propertySource, this.textEncryptor);
        EncryptablePropertySourcesConfigurer.LOGGER.info("makeEncryptable(): Converting PropertySource {}[{}] to {}", propertySource.getName(), propertySource.getClass()
                .getName(), encryptablePropertySource.getClass().getSimpleName());
        return encryptablePropertySource;
    }

    private PropertySource<?> instantiatePropertySource(PropertySource<?> propertySource, TextEncryptor textEncryptor) {
        PropertySource<?> encryptablePropertySource;
        if (propertySource instanceof MapPropertySource) {
            encryptablePropertySource = new EncryptableMapPropertySourceWrapper((MapPropertySource) propertySource, textEncryptor);
        } else if (propertySource instanceof EnumerablePropertySource) {
            encryptablePropertySource = new EncryptableEnumerablePropertySourceWrapper<>((EnumerablePropertySource<?>) propertySource, textEncryptor);
        } else {
            encryptablePropertySource = new EncryptablePropertySourceWrapper<>(propertySource, textEncryptor);
        }
        return encryptablePropertySource;
    }

    /**
     * @return the textEncryptor
     */
    public TextEncryptor getTextEncryptor() {
        return this.textEncryptor;
    }

    /**
     * @param textEncryptor
     *            the textEncryptor to set
     */
    public void setTextEncryptor(TextEncryptor textEncryptor) {
        this.textEncryptor = textEncryptor;
    }

    /**
     * @return the configurableEnvironment
     */
    public ConfigurableEnvironment getConfigurableEnvironment() {
        return this.configurableEnvironment;
    }

    /**
     * @param configurableEnvironment
     *            the configurableEnvironment to set
     */
    public void setConfigurableEnvironment(ConfigurableEnvironment configurableEnvironment) {
        this.configurableEnvironment = configurableEnvironment;
    }

}
