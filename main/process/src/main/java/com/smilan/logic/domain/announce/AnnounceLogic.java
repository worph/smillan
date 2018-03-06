/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.logic.domain.announce;

import com.smilan.api.domain.announce.AnnounceManagerDTO;
import com.smilan.api.domain.announce.dto.GeoSearchParameters;
import com.smilan.api.domain.announce.dto.GeoSearchResult;
import com.smilan.logic.common.EntityLogic;
import java.util.List;

/**
 *
 * @author Worph
 */
public interface AnnounceLogic extends EntityLogic<AnnounceManagerDTO> {
    public GeoSearchResult geoSearch(GeoSearchParameters param);
    public List<Long> getAnnounceId(Long profileId);
    public List<Long> idSearch(AnnounceManagerDTO param);
}
