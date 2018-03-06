/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.api.domain.announce.dto;

import java.io.Serializable;
import java.util.List;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 *
 * @author pierr
 */
@GeneratePojoBuilder(withCopyMethod = true, intoPackage = "*.builder")
public class GeoSearchResult implements Serializable {
    
    @GeneratePojoBuilder(withCopyMethod = true, intoPackage = "*.builder")
    public static class GeoSearchElement{
        private String id;
        private String lat;
        private String lng;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }
        
    }
    
    private List<GeoSearchElement> announces;

    public List<GeoSearchElement> getAnnounces() {
        return announces;
    }

    public void setAnnounces(List<GeoSearchElement> announces) {
        this.announces = announces;
    }
    
}
