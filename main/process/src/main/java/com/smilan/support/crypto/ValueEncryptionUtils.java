package com.smilan.support.crypto;

import org.springframework.security.crypto.encrypt.TextEncryptor;

import com.google.common.base.Preconditions;

/**
 * @author Thomas
 *
 */
public final class ValueEncryptionUtils {

    private static final String ENCRYPTED_VALUE_PREFIX = "ENC(";

    private static final String ENCRYPTED_VALUE_SUFFIX = ")";

    /** Private default constructor: cannot be instantiated */
    private ValueEncryptionUtils() {
    }

    public static boolean isEncryptedValue(final String value) {
        Preconditions.checkNotNull(value, "value must not be null");
        return value.startsWith(ValueEncryptionUtils.ENCRYPTED_VALUE_PREFIX) && value.endsWith(ValueEncryptionUtils.ENCRYPTED_VALUE_SUFFIX);
    }

    private static String getInnerEncryptedValue(final String value) {
        return value.substring(ValueEncryptionUtils.ENCRYPTED_VALUE_PREFIX.length(), value.length() - ValueEncryptionUtils.ENCRYPTED_VALUE_SUFFIX.length());
    }

    public static String decrypt(final String encodedValue, final TextEncryptor encryptor) {
        Preconditions.checkNotNull(encodedValue, "encodedValue must not be null");
        return encryptor.decrypt(ValueEncryptionUtils.getInnerEncryptedValue(encodedValue.trim()));
    }

    public static String encrypt(final String decodedValue, final TextEncryptor encryptor) {
        Preconditions.checkNotNull(decodedValue, "decodedValue must not be null");
        return ValueEncryptionUtils.ENCRYPTED_VALUE_PREFIX + encryptor.encrypt(decodedValue) + ValueEncryptionUtils.ENCRYPTED_VALUE_SUFFIX;
    }

}
