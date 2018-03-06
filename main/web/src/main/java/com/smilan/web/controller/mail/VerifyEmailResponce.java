/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.web.controller.mail;

import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 *
 * @author pierr
 */

@GeneratePojoBuilder(withCopyMethod = true, intoPackage = "*.builder")
public class VerifyEmailResponce {
    private boolean valide;

    public boolean isValide() {
        return valide;
    }

    public void setValide(boolean valide) {
        this.valide = valide;
    }
    
    
}
