package com.smilan.support.property.crypto;

import org.springframework.core.env.PropertySource;
import org.springframework.security.crypto.encrypt.TextEncryptor;

import com.smilan.support.crypto.ValueEncryptionUtils;

/**
 * @author Ulises Bocchio
 */
public interface EncryptablePropertySource<T> {

    public default Object getProperty(TextEncryptor textEncryptor, PropertySource<T> source, String name) {
        Object value = source.getProperty(name);
        if (value instanceof String) {
            String stringValue = String.valueOf(value);
            if (ValueEncryptionUtils.isEncryptedValue(stringValue)) {
                value = ValueEncryptionUtils.decrypt(stringValue, textEncryptor);
            }
        }
        return value;
    }
}
