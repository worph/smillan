/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.logic.domain.announce.geolocalisation;

import org.hibernate.Hibernate;
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

/**
 *
 * @author pierr
 */
public class MySqlDialectExtended extends MySQLDialect {
    
    /**
     * compute distance between 2 geolocation
     *   ?1 orig_lat (float)
     *   ?2 orig_lon (float)
     *   ?3 dest_lat (float)
     *   ?4 dest_lon (float)
     */
    public static final String geodistFunction = "geodist";

    public MySqlDialectExtended() {
        super();
        //TODO optim store the method in the DB (note this format only replace the function call bay the string defined herein) see https://fr.scribd.com/presentation/2569355/Geo-Distance-Search-with-MySQL
        registerFunction(geodistFunction, new SQLFunctionTemplate(StandardBasicTypes.FLOAT, "( 6371 * acos( cos( radians(?1) ) * cos( radians( ?3 ) ) * cos( radians( ?4 ) - radians(?2) ) + sin( radians(?1) ) * sin( radians( ?3 ) ) ) )"));//JPQL
    }

}
