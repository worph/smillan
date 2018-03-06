/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.logic.domain.announce;

import com.smilan.api.common.manager.option.OptionService;
import com.smilan.api.common.manager.option.SearchOption;
import com.smilan.api.domain.announce.AnnounceManagerDTO;
import com.smilan.api.domain.announce.dto.GeoSearchParameters;
import com.smilan.api.domain.announce.dto.GeoSearchResult;
import com.smilan.logic.common.EntityDAO;
import com.smilan.logic.domain.announce.entity.AnnounceEntity;
import java.util.List;

/**
 *
 * @author pierr
 */
public interface AnnounceDAOInterface extends EntityDAO<AnnounceEntity>{
    public GeoSearchResult geoSearch(GeoSearchParameters param);
    public List<Long> getAnnounceId(Long profileId);
    public List<Long> idSearch(List<AnnounceEntity> param,OptionService optionService);
}
