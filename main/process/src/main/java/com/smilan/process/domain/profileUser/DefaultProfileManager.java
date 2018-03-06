package com.smilan.process.domain.profileUser;

import com.google.common.base.Preconditions;
import com.smilan.api.domain.profile.Profile;
import com.smilan.api.domain.profile.ProfileManager;
import com.smilan.api.domain.profile.ProfileManagerDTO;
import com.smilan.logic.domain.profile.ProfileLogic;
import com.smilan.logic.domain.security.Realm;
import java.util.ArrayList;
import java.util.List;
import net.karneim.pojobuilder.GeneratePojoBuilder;
import org.springframework.transaction.annotation.Transactional;

@GeneratePojoBuilder(withCopyMethod = true, intoPackage = "*.builder")
public class DefaultProfileManager implements ProfileManager {

    private ProfileLogic userLogic;
    private Realm realm;

    @Transactional
    @Override
    public ProfileManagerDTO define(ProfileManagerDTO gererPersonneDTO) {
        Preconditions.checkNotNull(gererPersonneDTO, "gererPersonneDTO must not be null");
        //store updated information
        List<String> updatedId = new ArrayList<>();
        for (Profile entity : gererPersonneDTO.getEntities()) {
            if (entity.getId() != null) {
                updatedId.add(entity.getId());
            }
        }
        final ProfileManagerDTO define = userLogic.define(gererPersonneDTO);
        //update permission
        for (Profile entity : define.getEntities()) {
            final String id = entity.getId();
            if (!updatedId.contains(id)) {
                //this is a creation
                realm.setPermissionForNewProfile(id,entity.getUserId());
            }
        }
        return define;
    }

    @Transactional(readOnly = true)
    @Override
    public ProfileManagerDTO search(ProfileManagerDTO gererPersonneDTO) {
        Preconditions.checkNotNull(gererPersonneDTO, "gererPersonneDTO must not be null");
        return userLogic.search(gererPersonneDTO);
    }

    public ProfileLogic getProfileLogic() {
        return userLogic;
    }

    public void setProfileLogic(ProfileLogic userLogic) {
        this.userLogic = userLogic;
    }

    @Override
    public ProfileManagerDTO delete(ProfileManagerDTO manageDTO) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Realm getRealm() {
        return realm;
    }

    public void setRealm(Realm realm) {
        this.realm = realm;
    }

}
