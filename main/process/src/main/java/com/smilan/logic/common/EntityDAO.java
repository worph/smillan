package com.smilan.logic.common;

import com.smilan.logic.domain.security.CrudPermission;
import com.smilan.api.common.manager.option.DeleteOption;
import com.smilan.api.common.manager.option.Option;
import com.smilan.api.common.manager.option.OptionService;
import com.smilan.api.common.manager.option.SearchOption;
import java.util.List;

/**
 * @author Thomas
 *
 */
public interface EntityDAO<T> {

    /**
     * Persist or merge the given personne entity if it is a newly or managed personne entity.
     *
     * @param personne the given personne entity to persist or merge
     * @return the persisted or marged personne entity
     */
    T save(T entity);

    /**
     * Load the persisted personne entity from her id.
     *
     * @param personne The personne entity to load with her id.
     * @return the loaded personne entity.
     */
    T load(T entity);

    /**
     * Search personne entity by criteria and search option.
     *
     * @param personneCriteria list of personne criteria.
     * @param optionRecherche Search option.
     * @return The result list of the search. If no result are found the list is empty.
     */
    List<T> search(List<T> personneCriteria, OptionService options);
    
    CrudPermission getCrudPermission();

}
