/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.api.common.support;

import java.util.Properties;
import org.springframework.core.env.Environment;

/**
 *
 * @author pierr
 */
public class PropertyHelper {
    public static void putIfPresent(Properties jpaProperties, Environment environment, String propertyName) {
        String value = environment.getProperty(propertyName);
        if (value != null) {
            jpaProperties.put(propertyName, value);
        }
    }
}
