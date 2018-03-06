package com.smilan.api.common.support;

/**
 * @author Thomas
 *
 */
public interface ConstraintViolationValue {

    public final String LEVEL_INFO = "info";

    public final String LEVEL_ATTENTION = "attention";

    public final String LEVEL_ERREUR = "erreur";

    public final String EMAIL_REGEX = ".+@.+";

    public final String PHONE_REGEX = "^\\+?[1-9]\\d{1,14}$";

}
