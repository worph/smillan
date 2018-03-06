/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.process.commun.support;

import com.smilan.api.domain.announce.Announce;
import com.smilan.api.domain.announce.AnnounceManager;
import com.smilan.api.domain.announce.GeoLocation;
import com.smilan.api.domain.announce.builder.AnnounceBuilder;
import com.smilan.api.domain.announce.builder.AnnounceManagerDTOBuilder;
import com.smilan.api.domain.announce.builder.GeoLocationBuilder;
import com.smilan.api.domain.chat.ChatManager;
import com.smilan.api.domain.media.Media;
import com.smilan.api.domain.media.MediaManager;
import com.smilan.api.domain.media.MediaType;
import com.smilan.api.domain.media.builder.MediaBuilder;
import com.smilan.api.domain.profile.AnonymousUserGenerator;
import com.smilan.api.domain.profile.Profile;
import com.smilan.api.domain.profile.ProfileManager;
import com.smilan.api.domain.profile.builder.ProfileBuilder;
import com.smilan.api.domain.profile.builder.ProfileManagerDTOBuilder;
import com.smilan.api.domain.user.UserManager;
import com.smilan.process.domain.security.AdministrationExcutor;
import de.svenjacobs.loremipsum.LoremIpsum;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javax.annotation.PostConstruct;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.mgt.SecurityManager;

/**
 *
 * @author Pierre-Henri
 */
public class DummyPopulator {

    private final AnnounceManager announceManager;
    private final ProfileManager profileManager;
    private final MediaManager mediaManager;
    private final AnonymousUserGenerator anonymousUserGenerator;
    private final AdministrationExcutor administrationExcutor;

    public DummyPopulator(AnnounceManager announceManager, ProfileManager profileManager, MediaManager mediaManager, AnonymousUserGenerator anonymousUserGenerator, AdministrationExcutor administrationExcutor) {
        this.announceManager = announceManager;
        this.profileManager = profileManager;
        this.mediaManager = mediaManager;
        this.anonymousUserGenerator = anonymousUserGenerator;
        this.administrationExcutor = administrationExcutor;
    }

    @PostConstruct
    public void init() {
        administrationExcutor.run(() -> {
            populateAvatar();
            System.out.println("populate avatar");
        });
    }

    int anounceImageNumber = 19;
    String announceImagePath = "https://media.smillan.com/image/template/announce/";//TODO Put this in properties
    int backGroundImageNumber = 19;
    String defaultBackGroundPath = "https://media.smillan.com/image/template/background/";//TODO Put this in properties
    int avatarNumber = 72;
    String defaultAvatarPath = "https://media.smillan.com/image/template/avatar/";
    Random r = new Random(42);

    public float nexDeltaFloat(float value) {
        return r.nextBoolean() ? -r.nextFloat() * value : r.nextFloat() * value;
    }

    public void populateAvatar() {
        //create and store default user avatar
        {
            for (int i = 0; i < avatarNumber; i++) {
                //String imagePath = announceImagePath+"/announce-" + String.format("%03d", r.nextInt(19)) + ".jpg";
                final String url = defaultAvatarPath + "avatar-" + String.format("%03d", i) + ".jpeg";
                mediaManager.addDefaultAvatar(MediaType.IMAGE, url);
            }
        }
        //create and store default user background
        {
            for (int i = 0; i < backGroundImageNumber; i++) {
                String url = defaultBackGroundPath + "background-" + String.format("%03d", i) + ".jpg";
                mediaManager.addDefaultBackGround(MediaType.IMAGE, url);
            }
        }
    }

    public void populateDummy() {
        //create 50 new users
        for (int i = 0; i < 50; i++) {
            anonymousUserGenerator.createNewUser();
        }
        //create 50 announce
        {
            float lat = 49.050965999999995f;
            float lon = 2.100645f;
            LoremIpsum loremIpsum = new LoremIpsum();
            List<Announce> announces = new ArrayList<Announce>();
            final List<Profile> findAllProfiles = profileManager.search(new ProfileManagerDTOBuilder().withEntities(Arrays.asList(new ProfileBuilder().build())).build()).getEntities();
            for (int i = 0; i < 50; i++) {
                boolean hasmedia = r.nextBoolean();
                boolean hastext = r.nextBoolean();
                if(!hastext && !hasmedia){
                    hastext=true;
                }
                List<Media> announceMedia = new ArrayList<>();
                if (hasmedia) {
                    announceMedia.add(new MediaBuilder().withType(MediaType.IMAGE).withUrl(announceImagePath + "announce-" + String.format("%03d", r.nextInt(anounceImageNumber)) + ".jpg").build());
                }
                List<GeoLocation> locations = new ArrayList<>();
                locations.add(new GeoLocationBuilder().withLat(lat + nexDeltaFloat(0.2f)).withLon(lon + nexDeltaFloat(0.2f)).build());
                String title = i+"";
                final int nextInt = r.nextInt(3);
                String type="";
                switch(nextInt){
                    case 0 :type="snapan";break;
                    case 1 :type="genan";break;
                    case 2 :type="longan";break;
                }
                Announce announce = new AnnounceBuilder()
                        .withId(null)
                        .withTitle(title)
                        .withType(type)
                        .withText(hastext ? loremIpsum.getWords(r.nextInt(50) + 10) : null)
                        .withProfileId(findAllProfiles.get(r.nextInt(findAllProfiles.size() - 1)).getId())
                        .withMedia(announceMedia)
                        .withLocations(locations)
                        .build();
                announces.add(announce);
            }
            announceManager.define(new AnnounceManagerDTOBuilder().withEntities(announces).build());
        }
        //make new friends
        /*{
            final List<Profile> findAllUsers = profileManager.search(new ProfileManagerDTOBuilder().withEntities(Arrays.asList(new ProfileBuilder().build())).build()).getEntities();
            for (Profile currentUser : findAllUsers) {
                for (int i = 0; i < 5; i++) {
                    final String user1 = currentUser.getXmppId();
                    String user2 = user1;
                    while (user1.equals(user2)) {
                        user2 = findAllUsers.get(r.nextInt(findAllUsers.size() - 1)).getXmppId();
                    }
                    chatManager.linkUsers(user1, user2);
                }
            }
        }*/
    }

}
