/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.api.domain.announce.dto;

import java.io.Serializable;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 *
 * @author pierr
 */
@GeneratePojoBuilder(withCopyMethod = true, intoPackage = "*.builder")
public class GeoSearchParameters implements Serializable {
    private float lat;
    private float lon;
    private float dist;

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

    public float getDist() {
        return dist;
    }

    public void setDist(float dist) {
        this.dist = dist;
    }
    
}
