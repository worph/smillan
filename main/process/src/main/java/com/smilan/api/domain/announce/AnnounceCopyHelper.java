package com.smilan.api.domain.announce;

import com.smilan.api.common.support.CommunHelper;
import com.smilan.api.domain.announce.builder.AnnounceBuilder;
import com.smilan.api.domain.announce.builder.AnnounceManagerDTOBuilder;
import java.util.ArrayList;
import java.util.List;


public class AnnounceCopyHelper {

    private final CommunHelper communHelper = new CommunHelper();

    public AnnounceManagerDTO copy(AnnounceManagerDTO gererAnnounceDTO) {
        if (gererAnnounceDTO == null) {
            return null;
        }
        return new AnnounceManagerDTOBuilder().copy(gererAnnounceDTO)
                .withOptionService(this.communHelper.copy(gererAnnounceDTO.getOptionService()))
                .withEntities(this.copy(gererAnnounceDTO.getEntities()))
                .build();
    }

    public List<Announce> copy(List<Announce> personnes) {
        if (personnes == null) {
            return null;
        }
        List<Announce> clonedAnnounces = new ArrayList<>();
        for (Announce personne : personnes) {
            clonedAnnounces.add(this.copy(personne));
        }
        return clonedAnnounces;
    }

    public Announce copy(Announce personne) {
        if (personne == null) {
            return null;
        }
        return new AnnounceBuilder().copy(personne).build();
    }

}
