/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.logic.domain.announce;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.smilan.api.domain.announce.Announce;
import com.smilan.api.domain.announce.AnnounceManagerDTO;
import com.smilan.api.domain.announce.builder.AnnounceManagerDTOBuilder;
import com.smilan.api.domain.announce.dto.GeoSearchParameters;
import com.smilan.api.domain.announce.dto.GeoSearchResult;
import com.smilan.api.domain.category.Category;
import com.smilan.logic.domain.security.CrudPermission;
import com.smilan.logic.common.DefaultEntityLogic;
import com.smilan.logic.domain.announce.entity.AnnounceEntity;
import com.smilan.logic.domain.category.CategoryDAOI;
import com.smilan.logic.domain.category.entity.CategoryEntity;
import com.smilan.logic.domain.chat.XMPPService;
import com.smilan.logic.domain.security.CategoryPermision;
import com.smilan.logic.domain.security.Realm;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Worph
 */
public class DefaultAnnounceLogic extends DefaultEntityLogic<AnnounceEntity, Announce, AnnounceManagerDTO> implements AnnounceLogic {

    private final AnnounceDAOInterface announceDAOInterface;

    public DefaultAnnounceLogic(
            AnnounceDAOInterface announceDAO,
            Function<Announce, AnnounceEntity> announceToAnnounceEntityFunction,
            Function<AnnounceEntity, Announce> announceEntityToAnnounceFunction) {
        super(announceDAO, announceToAnnounceEntityFunction, announceEntityToAnnounceFunction);
        this.announceDAOInterface = announceDAO;
    }

    @Override
    public AnnounceManagerDTO buildEntityManagerDTO(List<Announce> announces) {
        return new AnnounceManagerDTOBuilder().withEntities(announces).build();
    }

    @Override
    public GeoSearchResult geoSearch(GeoSearchParameters param) {
        return announceDAOInterface.geoSearch(param);
    }

    @Override
    public List<Long> getAnnounceId(Long profileId) {
        return announceDAOInterface.getAnnounceId(profileId);
    }

    @Override
    public void securityFilterData(Announce entity, CrudPermission crudPermission) {
        //always authorized
    }

    @Override
    public List<Long> idSearch(AnnounceManagerDTO param) {
        final List<AnnounceEntity> transform = Lists.transform(param.getEntities(), apiToEntityFunction);
        return announceDAOInterface.idSearch(transform, param.getOptionService());
    }

}
