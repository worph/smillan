/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.api.domain.announce;

import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 *
 * @author pierr
 */

@GeneratePojoBuilder(withCopyMethod = true, intoPackage = "*.builder")
public class GeoLocation {
    private float lat;
    private float lon;

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Float.floatToIntBits(this.lat);
        hash = 97 * hash + Float.floatToIntBits(this.lon);
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
        final GeoLocation other = (GeoLocation) obj;
        if (Float.floatToIntBits(this.lat) != Float.floatToIntBits(other.lat)) {
            return false;
        }
        if (Float.floatToIntBits(this.lon) != Float.floatToIntBits(other.lon)) {
            return false;
        }
        return true;
    }
    
    
}
