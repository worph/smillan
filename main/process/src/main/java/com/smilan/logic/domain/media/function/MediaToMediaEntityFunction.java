package com.smilan.logic.domain.media.function;

import com.google.common.base.Function;
import com.smilan.api.domain.media.Media;
import com.smilan.logic.domain.media.MediaDAOInterface;
import com.smilan.logic.domain.media.entity.MediaEntity;
import com.smilan.logic.domain.media.entity.builder.MediaEntityBuilder;
import net.karneim.pojobuilder.GeneratePojoBuilder;

@GeneratePojoBuilder(withCopyMethod = true)
public class MediaToMediaEntityFunction implements Function<Media, MediaEntity> {
    private MediaDAOInterface mediaDAO;

    MediaToMediaEntityFunction() {
    }
    
    
    @Override
    public MediaEntity apply(Media input) {
        if(input==null) return null;
        return mediaDAO.retreiveOrCreate(input);
    }

    public MediaDAOInterface getMediaDAO() {
        return mediaDAO;
    }

    public void setMediaDAO(MediaDAOInterface mediaDAO) {
        this.mediaDAO = mediaDAO;
    }
    
}
