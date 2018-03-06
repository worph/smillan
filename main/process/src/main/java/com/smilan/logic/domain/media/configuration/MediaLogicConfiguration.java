package com.smilan.logic.domain.media.configuration;

import com.google.common.base.Function;
import com.smilan.api.domain.media.Media;
import com.smilan.logic.domain.security.CrudPermission;
import com.smilan.logic.common.EntityDAO;
import com.smilan.logic.common.configuration.EntityManagerConfiguration;
import com.smilan.logic.domain.media.DefaultMediaLogic;
import com.smilan.logic.domain.media.MediaDAOInterface;
import com.smilan.logic.domain.media.MediaLogic;
import com.smilan.logic.domain.media.builder.JPAMediaDAOBuilder;
import com.smilan.logic.domain.media.entity.MediaEntity;
import com.smilan.logic.domain.media.function.MediaEntityToMediaFunction;
import com.smilan.logic.domain.media.function.MediaToMediaEntityFunction;
import com.smilan.logic.domain.media.function.MediaToMediaEntityFunctionBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Thomas
 *
 */
@Configuration
public class MediaLogicConfiguration {

    @Bean
    public MediaDAOInterface mediaDAO(EntityManagerConfiguration entityManagerConfiguration) {
        return new JPAMediaDAOBuilder()
                .withEntityManager(entityManagerConfiguration.getEntityManager())
                .withCrudPermission(new CrudPermission("media"))
                .build();
    }

    @Bean
    public MediaLogic mediaLogic(EntityDAO<MediaEntity> personneDAO, Function<Media, MediaEntity> mediaToMediaEntityFunction,
            Function<MediaEntity, Media> mediaEntityToMediaFunction) {
        return new DefaultMediaLogic(personneDAO, mediaToMediaEntityFunction, mediaEntityToMediaFunction);
    }

    @Bean
    public Function<Media, MediaEntity> mediaToMediaEntityFunction(MediaDAOInterface mediaDAO) {
        MediaToMediaEntityFunction personneToAnnounceLogicFunction = new MediaToMediaEntityFunctionBuilder()
                .withMediaDAO(mediaDAO)
                .build();
        return personneToAnnounceLogicFunction;
    }

    @Bean
    public Function<MediaEntity, Media> mediaEntityToMediaFunction() {
        MediaEntityToMediaFunction personneLogicToAnnounceFunction = new MediaEntityToMediaFunction();
        return personneLogicToAnnounceFunction;
    }

}
