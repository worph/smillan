package com.smilan.support.property.crypto.wrapper;

import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.security.crypto.encrypt.TextEncryptor;

import com.google.common.base.Preconditions;

import com.smilan.support.property.crypto.EncryptablePropertySource;

/**
 * @author Thomas
 *
 * @param <T>
 *            source
 */
public class EncryptableEnumerablePropertySourceWrapper<T> extends EnumerablePropertySource<T> implements EncryptablePropertySource<T> {

    private final EnumerablePropertySource<T> delegate;

    private final TextEncryptor               textEncryptor;

    public EncryptableEnumerablePropertySourceWrapper(EnumerablePropertySource<T> delegate, TextEncryptor textEncryptor) {
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

    @Override
    public String[] getPropertyNames() {
        return this.delegate.getPropertyNames();
    }
}
