package com.smilan.api.domain.profile;

import com.smilan.api.domain.media.Media;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import net.karneim.pojobuilder.GeneratePojoBuilder;

@GeneratePojoBuilder(withCopyMethod = true, intoPackage = "*.builder")
public class Profile implements Serializable {

    private String id;
    private String userId;
    private String profileName;
    private String xmppId;
    private String description;
    private Media avatar;
    private Media background;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getXmppId() {
        return xmppId;
    }

    public void setXmppId(String xmppId) {
        this.xmppId = xmppId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Media getAvatar() {
        return avatar;
    }

    public void setAvatar(Media avatar) {
        this.avatar = avatar;
    }

    public Media getBackground() {
        return background;
    }

    public void setBackground(Media background) {
        this.background = background;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Profile other = (Profile) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Profile{" + "id=" + id + ", userId=" + userId + ", profileName=" + profileName + ", xmppId=" + xmppId + ", description=" + description + ", avatar=" + avatar + ", background=" + background + '}';
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
