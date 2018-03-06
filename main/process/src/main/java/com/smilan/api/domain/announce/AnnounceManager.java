/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.api.domain.announce;

import com.smilan.api.common.manager.EntityManager;
import com.smilan.api.domain.announce.dto.GeoSearchParameters;
import com.smilan.api.domain.announce.dto.GeoSearchResult;
import java.util.List;

/**
 *
 * @author Worph
 */
public interface AnnounceManager extends EntityManager<AnnounceManagerDTO> {
    public GeoSearchResult geoSearch(GeoSearchParameters param);
    public List<String> idSearch(AnnounceManagerDTO param);

    public void cleanUpAnnounces();

}
