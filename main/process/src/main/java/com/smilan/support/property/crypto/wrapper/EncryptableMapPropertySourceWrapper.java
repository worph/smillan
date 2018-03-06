package com.smilan.support.property.crypto.wrapper;

import java.util.Map;

import org.springframework.core.env.MapPropertySource;
import org.springframework.security.crypto.encrypt.TextEncryptor;

import com.google.common.base.Preconditions;

import com.smilan.support.property.crypto.EncryptablePropertySource;

/**
 * @author Ulises Bocchio
 */
public class EncryptableMapPropertySourceWrapper extends MapPropertySource implements EncryptablePropertySource<Map<String, Object>> {

    private final MapPropertySource delegate;

    private final TextEncryptor     textEncryptor;

    public EncryptableMapPropertySourceWrapper(MapPropertySource delegate, TextEncryptor textEncryptor) {
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
