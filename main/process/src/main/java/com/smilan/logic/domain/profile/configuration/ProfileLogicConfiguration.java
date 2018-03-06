package com.smilan.logic.domain.profile.configuration;

import com.google.common.base.Function;
import com.smilan.api.domain.media.Media;
import com.smilan.api.domain.profile.Profile;
import com.smilan.logic.domain.security.CrudPermission;
import com.smilan.logic.common.configuration.EntityManagerConfiguration;
import com.smilan.logic.domain.media.configuration.MediaLogicConfiguration;
import com.smilan.logic.domain.media.entity.MediaEntity;
import com.smilan.logic.domain.media.function.MediaEntityToMediaFunction;
import com.smilan.logic.domain.profile.DefaultProfileLogic;
import com.smilan.logic.domain.profile.ProfileDAOInterface;
import com.smilan.logic.domain.profile.ProfileLogic;
import com.smilan.logic.domain.profile.builder.JPAProfileDAOBuilder;
import com.smilan.logic.domain.profile.entity.ProfileEntity;
import com.smilan.logic.domain.profile.function.ProfileEntityToProfileFunction;
import com.smilan.logic.domain.profile.function.ProfileToProfileEntityFunction;
import com.smilan.logic.domain.profile.function.builder.ProfileEntityToProfileFunctionBuilder;
import com.smilan.logic.domain.profile.function.builder.ProfileToProfileEntityFunctionBuilder;
import com.smilan.logic.domain.security.Realm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Thomas
 *
 */
@Configuration
@Import({
    MediaLogicConfiguration.class
})
public class ProfileLogicConfiguration {

    @Bean
    public ProfileDAOInterface profileDAO(EntityManagerConfiguration entityManagerConfiguration) {
        return new JPAProfileDAOBuilder()
                .withEntityManager(entityManagerConfiguration.getEntityManager())
                .withCrudPermission(new CrudPermission("profile"))
                .build();
    }

    @Bean
    public ProfileLogic profileLogic(
            ProfileDAOInterface profileDAO,
            Function<Profile, ProfileEntity> profileToProfileEntityFunction,
            Function<ProfileEntity, Profile> profileEntityToProfileFunction) {
        return new DefaultProfileLogic(profileDAO, profileToProfileEntityFunction, profileEntityToProfileFunction);
    }

    @Bean
    public Function<Profile, ProfileEntity> profileToProfileEntityFunction(Function<Media, MediaEntity> mediaToMediaEntityFunction) {
        ProfileToProfileEntityFunction profileToProfileEntityFunction = new ProfileToProfileEntityFunctionBuilder()
                .withMedia(mediaToMediaEntityFunction)
                .build();
        return profileToProfileEntityFunction;
    }

    @Bean
    public Function<ProfileEntity, Profile> profileEntityToProfileFunction() {
        ProfileEntityToProfileFunction profileEntityToProfileFunction = new ProfileEntityToProfileFunctionBuilder().withMedia(new MediaEntityToMediaFunction()).build();
        return profileEntityToProfileFunction;
    }

}
