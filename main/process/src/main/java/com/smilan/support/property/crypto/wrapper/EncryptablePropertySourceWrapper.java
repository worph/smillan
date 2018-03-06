package com.smilan.support.property.crypto.wrapper;

import org.springframework.core.env.PropertySource;
import org.springframework.security.crypto.encrypt.TextEncryptor;

import com.google.common.base.Preconditions;

import com.smilan.support.property.crypto.EncryptablePropertySource;

/**
 * <p>
 * Wrapper for {@link PropertySource} instances that simply delegates the {@link #getProperty} method to the {@link PropertySource} delegate instance to retrieve properties, while checking if the resulting property is encrypted or not using the Jasypt convention of surrounding encrypted values with "ENC()".
 * </p>
 * <p>
 * When an encrypted property is detected, it is decrypted using the provided {@link TextEncryptor}
 * </p>
 *
 * @author Thomas
 *
 * @param <T>
 *            source
 */
public class EncryptablePropertySourceWrapper<T> extends PropertySource<T> implements EncryptablePropertySource<T> {

    private final PropertySource<T> delegate;

    private final TextEncryptor     textEncryptor;

    public EncryptablePropertySourceWrapper(PropertySource<T> delegate, TextEncryptor textEncryptor) {
        super(delegate.getName(), delegate.getSource());
        Preconditions.checkNotNull(delegate, "PropertySource delegate must not be null");
        Preconditions.checkNotNull(textEncryptor, "TextEncryptor textEncryptor must not be null");
        this.delegate = delegate;
        this.textEncryptor = textEncryptor;
    }

    @Override
    public Object getProperty(String name) {
        return this.getProperty(this.textEncryptor, this.delegate, name);
    }
}
