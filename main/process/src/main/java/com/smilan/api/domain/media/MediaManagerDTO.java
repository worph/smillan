/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.api.domain.media;

import com.smilan.api.common.manager.EntityManagerDTO;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 *
 * @author Worph
 */
@GeneratePojoBuilder(withCopyMethod = true, intoPackage = "*.builder")
public class MediaManagerDTO extends EntityManagerDTO<Media> {

}
