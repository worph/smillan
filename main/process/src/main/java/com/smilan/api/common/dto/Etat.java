package com.smilan.api.common.dto;

import java.io.Serializable;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * Définition commune au SI d'un Etat
 *
 * @author Thomas
 *
 */
@GeneratePojoBuilder(withCopyMethod = true, intoPackage = "*.builder")
public class Etat implements Serializable {

    /** Generated SerialVersionUID */
    private static final long serialVersionUID = 7703660730931239288L;

    private String            identifiant;

    private String            code;

    /** Contexte de l'appel au service ayant créé cette état */
    private Contexte          contexte;

    /** Default constructor */
    public Etat() {
    }

    /**
     * @return the identifiant
     */
    public String getIdentifiant() {
        return this.identifiant;
    }

    /**
     * @param identifiant the identifiant to set
     */
    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return this.code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the contexte
     */
    public Contexte getContexte() {
        return this.contexte;
    }

    /**
     * @param contexte the contexte to set
     */
    public void setContexte(Contexte contexte) {
        this.contexte = contexte;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Etat [identifiant=").append(this.identifiant).append(", code=").append(this.code).append(", contexte=").append(this.contexte).append("]");
        return builder.toString();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.identifiant == null ? 0 : this.identifiant.hashCode());
        return result;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        Etat other = (Etat) obj;
        if (this.identifiant == null) {
            if (other.identifiant != null) {
                return false;
            }
        } else if (!this.identifiant.equals(other.identifiant)) {
            return false;
        }
        return true;
    }

}
