package com.smilan.logic.common;

import com.smilan.api.common.manager.EntityManagerDTO;

public interface EntityLogic<T extends EntityManagerDTO> {

    T define(T gererPersonneDTO);

    T search(T gererPersonneDTO);

}
