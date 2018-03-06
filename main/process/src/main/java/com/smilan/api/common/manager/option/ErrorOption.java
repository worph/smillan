package com.smilan.api.common.manager.option;

import java.io.Serializable;
import java.util.Objects;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * Option générique d'appel au service de recherche
 *
 * @author Thomas
 *
 */
@GeneratePojoBuilder(withCopyMethod = true, intoPackage = "*.builder")
public class ErrorOption implements Serializable,Option {

    private String            error;

    /** Default constructor */
    public ErrorOption() {
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.error);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ErrorOption other = (ErrorOption) obj;
        if (!Objects.equals(this.error, other.error)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ErrorOption{" + "error=" + error + '}';
    }
    
}
