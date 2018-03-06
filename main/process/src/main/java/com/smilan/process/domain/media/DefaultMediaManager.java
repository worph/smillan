package com.smilan.process.domain.media;

import com.google.common.base.Preconditions;
import com.smilan.api.domain.media.Media;
import com.smilan.api.domain.media.MediaManager;
import com.smilan.api.domain.media.MediaManagerDTO;
import com.smilan.api.domain.media.builder.MediaBuilder;
import com.smilan.api.domain.media.builder.MediaManagerDTOBuilder;
import com.smilan.logic.domain.media.MediaLogic;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.karneim.pojobuilder.GeneratePojoBuilder;
import org.springframework.transaction.annotation.Transactional;

@GeneratePojoBuilder(withCopyMethod = true, intoPackage = "*.builder")
public class DefaultMediaManager implements MediaManager {

    private MediaLogic mediaLogic;
    private final List<Media> defaultAvatar = new ArrayList<>();//TODO create a table in DB to store this
    private final List<Media> defaultBackGround = new ArrayList<>();//TODO create a table in DB to store this
    
    @Override
    public List<Media> getDefaultAvatarList(){
        return new ArrayList<>(defaultAvatar);//load this from a table
    }
    
    
    @Override
    @Transactional
    public void addDefaultAvatar(String type,String url){
        //store to BDD
        final Media build = new MediaBuilder().withId(null).withType(type).withUrl(url).build();
        final MediaManagerDTO define = mediaLogic.define(new MediaManagerDTOBuilder().withEntities(Arrays.asList(build)).build());
        defaultAvatar.add(define.getEntities().get(0));
    }    
    
    @Override
    public List<Media> getDefaultBackGroundList(){
        return new ArrayList<>(defaultBackGround);//load this from a table
    }
    
    
    @Override
    @Transactional
    public void addDefaultBackGround(String type,String url){
        //store to BDD
        final Media build = new MediaBuilder().withId(null).withType(type).withUrl(url).build();
        final MediaManagerDTO define = mediaLogic.define(new MediaManagerDTOBuilder().withEntities(Arrays.asList(build)).build());
        defaultBackGround.add(define.getEntities().get(0));
    }

    @Transactional
    @Override
    public MediaManagerDTO define(MediaManagerDTO mediaDTO) {
        Preconditions.checkNotNull(mediaDTO, "gererPersonneDTO must not be null");
        return mediaLogic.define(mediaDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public MediaManagerDTO search(MediaManagerDTO mediaDTO) {
        Preconditions.checkNotNull(mediaDTO, "gererPersonneDTO must not be null");
        return mediaLogic.search(mediaDTO);
    }

    public MediaLogic getMediaLogic() {
        return mediaLogic;
    }

    public void setMediaLogic(MediaLogic mediaLogic) {
        this.mediaLogic = mediaLogic;
    }

    @Override
    public MediaManagerDTO delete(MediaManagerDTO manageDTO) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
