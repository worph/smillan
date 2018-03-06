package com.smilan.logic.domain.announce.configuration;

import com.google.common.base.Function;
import com.smilan.api.domain.announce.Announce;
import com.smilan.api.domain.category.Category;
import com.smilan.api.domain.media.Media;
import com.smilan.logic.domain.security.CrudPermission;
import com.smilan.logic.common.configuration.EntityManagerConfiguration;
import com.smilan.logic.domain.announce.AnnounceDAOInterface;
import com.smilan.logic.domain.announce.AnnounceLogic;
import com.smilan.logic.domain.announce.DefaultAnnounceLogic;
import com.smilan.logic.domain.announce.builder.JPAAnnounceDAOBuilder;
import com.smilan.logic.domain.announce.entity.AnnounceEntity;
import com.smilan.logic.domain.announce.function.AnnounceEntityToAnnounceFunction;
import com.smilan.logic.domain.announce.function.AnnounceToAnnounceEntityFunction;
import com.smilan.logic.domain.announce.function.builder.AnnounceEntityToAnnounceFunctionBuilder;
import com.smilan.logic.domain.announce.function.builder.AnnounceToAnnounceEntityFunctionBuilder;
import com.smilan.logic.domain.category.CategoryDAOI;
import com.smilan.logic.domain.category.configuration.CategoryLogicConfiguration;
import com.smilan.logic.domain.category.entity.CategoryEntity;
import com.smilan.logic.domain.category.function.CategoryEntityToCategoryFunction;
import com.smilan.logic.domain.chat.XMPPService;
import com.smilan.logic.domain.chat.configuration.ChatLogicConfiguration;
import com.smilan.logic.domain.media.configuration.MediaLogicConfiguration;
import com.smilan.logic.domain.media.entity.MediaEntity;
import com.smilan.logic.domain.media.function.MediaEntityToMediaFunction;
import com.smilan.logic.domain.media.function.MediaToMediaEntityFunction;
import com.smilan.logic.domain.security.Realm;
import com.smilan.logic.domain.security.configuration.RealmConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Thomas
 *
 */
@Configuration
@Import({
    CategoryLogicConfiguration.class,
    MediaLogicConfiguration.class
})
public class AnnounceLogicConfiguration {

    @Bean
    public AnnounceDAOInterface announceDAO(EntityManagerConfiguration entityManagerConfiguration) {
        return new JPAAnnounceDAOBuilder()
                .withEntityManager(entityManagerConfiguration.getEntityManager())
                .withCrudPermission(new CrudPermission("announce"))
                .build();
    }

    @Bean
    public AnnounceLogic announceLogic(
            AnnounceDAOInterface announceDAO,
            Function<Announce, AnnounceEntity> personneToAnnounceEntityFunction,
            Function<AnnounceEntity, Announce> personneEntityToAnnounceFunction) {
        return new DefaultAnnounceLogic(announceDAO, personneToAnnounceEntityFunction, personneEntityToAnnounceFunction);
    }

    @Bean
    public Function<Announce, AnnounceEntity> announceToAnnounceLogicFunction(
            Function<Category, CategoryEntity> categoryToCategoryEntityFunction,
            Function<Media, MediaEntity> mediaToMediaEntityFunction) {
        AnnounceToAnnounceEntityFunction personneToAnnounceLogicFunction = new AnnounceToAnnounceEntityFunctionBuilder()
                .withCategory(categoryToCategoryEntityFunction)
                .withMedia(mediaToMediaEntityFunction)
                .build();
        return personneToAnnounceLogicFunction;
    }

    @Bean
    public Function<AnnounceEntity, Announce> announceLogicToAnnounceFunction() {
        AnnounceEntityToAnnounceFunction personneLogicToAnnounceFunction = new AnnounceEntityToAnnounceFunctionBuilder()
                .withCategory(new CategoryEntityToCategoryFunction())
                .withMedia(new MediaEntityToMediaFunction())
                .build();
        return personneLogicToAnnounceFunction;
    }

}
