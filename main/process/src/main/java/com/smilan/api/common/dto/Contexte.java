package com.smilan.api.common.dto;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * @author Thomas
 *
 */
@GeneratePojoBuilder(withCopyMethod = true, intoPackage = "*.builder")
public class Contexte implements Serializable {

    /** Generated SerialVersionUID */
    private static final long serialVersionUID = 6294957420454955687L;

    /** Generated identifier at the information system entrance */
    private String            identifiantCorrelation;

    /** Timestamp de l'appel */
    private OffsetDateTime    timestamp;

    /** Identifiant acteur effectuant l'opération dans le système d'information */
    private String            identifiantActeur;

    /** Application effectuant l'opération dans le système d'information */
    private String            application;

    /** Default empty constructor */
    public Contexte() {
    }

    /**
     * @return the identifiantCorrelation
     */
    public String getIdentifiantCorrelation() {
        return this.identifiantCorrelation;
    }

    /**
     * @param identifiantCorrelation the identifiantCorrelation to set
     */
    public void setIdentifiantCorrelation(String identifiantCorrelation) {
        this.identifiantCorrelation = identifiantCorrelation;
    }

    /**
     * @return the timestamp
     */
    public OffsetDateTime getTimestamp() {
        return this.timestamp;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return the identifiantActeur
     */
    public String getIdentifiantActeur() {
        return this.identifiantActeur;
    }

    /**
     * @param identifiantActeur the identifiantActeur to set
     */
    public void setIdentifiantActeur(String identifiantActeur) {
        this.identifiantActeur = identifiantActeur;
    }

    /**
     * @return the application
     */
    public String getApplication() {
        return this.application;
    }

    /**
     * @param application the application to set
     */
    public void setApplication(String application) {
        this.application = application;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Contexte [identifiantCorrelation=").append(this.identifiantCorrelation).append(", timestamp=").append(this.timestamp).append(", identifiantActeur=")
                .append(this.identifiantActeur).append(", application=").append(this.application).append("]");
        return builder.toString();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.identifiantCorrelation == null ? 0 : this.identifiantCorrelation.hashCode());
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
        Contexte other = (Contexte) obj;
        if (!Objects.equals(this.identifiantCorrelation, other.identifiantCorrelation)) {
            return false;
        }
        return true;
    }
}
