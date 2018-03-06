package com.smilan.api.common.support;

import com.smilan.api.common.dto.Contexte;
import com.smilan.api.common.dto.Etat;
import com.smilan.api.common.dto.builder.ContexteBuilder;
import com.smilan.api.common.dto.builder.EtatBuilder;
import com.smilan.api.common.manager.option.OptionService;
import com.smilan.api.common.manager.option.SearchOption;
import com.smilan.api.common.manager.option.builder.OptionServiceBuilder;
import com.smilan.api.common.manager.option.builder.SearchOptionBuilder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Thomas
 *
 */
public class CommunHelper {

    public Contexte copy(Contexte contexte) {
        if (contexte == null) {
            return null;
        }
        return new ContexteBuilder().copy(contexte).build();
    }

    public Etat copy(Etat etat) {
        if (etat == null) {
            return null;
        }
        return new EtatBuilder().copy(etat)
                .withContexte(this.copy(etat.getContexte())).build();
    }

    public List<Etat> copy(List<Etat> etats) {
        if (etats == null) {
            return null;
        }
        List<Etat> clonedEtats = new ArrayList<>();
        for (Etat etat : etats) {
            clonedEtats.add(this.copy(etat));
        }
        return clonedEtats;
    }

    public OptionService copy(OptionService optionService) {
        if (optionService == null) {
            return null;
        }
        return new OptionServiceBuilder().copy(optionService)
                .withSearchOption(this.copy(optionService.getSearchOption())).build();
    }

    public SearchOption copy(SearchOption optionRecherche) {
        if (optionRecherche == null) {
            return null;
        }
        return new SearchOptionBuilder().copy(optionRecherche).build();
    }
}
