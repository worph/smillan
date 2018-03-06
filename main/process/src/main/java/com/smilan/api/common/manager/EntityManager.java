/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.api.common.manager;

public interface EntityManager<T extends EntityManagerDTO> {

    T define(T manageDTO);

    T search(T manageDTO);

    T delete(T manageDTO);
}
