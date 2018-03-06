package com.smilan.process.domain.profileUser;

import com.google.common.base.Preconditions;
import com.smilan.api.common.manager.DTOErrorHelper;
import com.smilan.api.domain.user.User;
import com.smilan.api.domain.user.UserAuthToken;
import com.smilan.api.domain.user.UserManager;
import com.smilan.api.domain.user.UserManagerDTO;
import com.smilan.api.domain.user.builder.UserBuilder;
import com.smilan.api.domain.user.builder.UserManagerDTOBuilder;
import com.smilan.logic.domain.security.Realm;
import static com.smilan.logic.domain.security.Realm.ROLE_ADMIN;
import com.smilan.logic.domain.user.UserLogic;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.karneim.pojobuilder.GeneratePojoBuilder;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.transaction.annotation.Transactional;

@GeneratePojoBuilder(withCopyMethod = true, intoPackage = "*.builder")
public class DefaultUserManager implements UserManager {

    private UserLogic userLogic;
    private Realm realm;

    @Transactional
    @Override
    public UserManagerDTO define(UserManagerDTO gererPersonneDTO) {
        Preconditions.checkNotNull(gererPersonneDTO, "gererPersonneDTO must not be null");
        //store updated information
        List<String> updatedId = new ArrayList<>();
        for (User entity : gererPersonneDTO.getEntities()) {
            if (entity.getId() != null) {
                updatedId.add(entity.getId());
            }
        }
        final UserManagerDTO define = userLogic.define(gererPersonneDTO);
        for (User entity : define.getEntities()) {
            final String id = entity.getId();
            if (!updatedId.contains(id)) {
                //this is a creation
                realm.setPermissionForNewUser(id);
            }
        }
        return define;
    }

    @Transactional(readOnly = true)
    @Override
    public UserManagerDTO search(UserManagerDTO gererPersonneDTO) {
        Preconditions.checkNotNull(gererPersonneDTO, "gererPersonneDTO must not be null");
        return userLogic.search(gererPersonneDTO);
    }

    @Override
    @Transactional
    public void changePassword(String login, String newPassword) {
        final Subject subject = SecurityUtils.getSubject();
        if (subject.hasRole(ROLE_ADMIN)) {
            final User user = userLogic.getUserFromUsername(login);
            userLogic.setPassword(user.getId(), newPassword);
        } else {
            throw new IllegalArgumentException("must be administrator because not old password has been provided");
        }
    }

    @Override
    @Transactional
    public void changeLoginPassword(String newLogin, String newPassword, String oldLogin, String oldPassword) {
        final Subject subject = SecurityUtils.getSubject();
        if (subject.hasRole(ROLE_ADMIN)) {
            final User user = userLogic.getUserFromUsername(oldLogin);
            userLogic.setLogin(user.getId(), newLogin);
            userLogic.setPassword(user.getId(), newPassword);
        } else {
            //ok regular user login pasword update process
            Preconditions.checkNotNull(realm);
            final User user = userLogic.getUserFromUsername(oldLogin);
            if (!user.getPassword().equals(oldPassword)) {
                throw new IllegalArgumentException();
            }
            //one at a time to keep credential ok
            //update login
            userLogic.setLogin(user.getId(), newLogin);
            realm.invalidateCurrentSubjectCache();
            subject.logout();
            subject.login(new UsernamePasswordToken(newLogin, oldPassword));

            //update password
            userLogic.setPassword(user.getId(), newPassword);
            realm.invalidateCurrentSubjectCache();
            subject.logout();
            subject.login(new UsernamePasswordToken(newLogin, newPassword));
        }

    }

    @Transactional(readOnly = true)
    @Override
    public User getUserFromUsername(String username) {
        return userLogic.getUserFromUsername(username);
    }

    @Transactional(readOnly = true)
    @Override
    public User getUserFromApiKey(String apiKey) {
        return userLogic.getUserFromApiKey(apiKey);
    }

    @Transactional(readOnly = true)
    @Override
    public UserManagerDTO authSearch(UserAuthToken authToken) {
        Preconditions.checkNotNull(authToken);
        Preconditions.checkNotNull(authToken.getLogin());
        Preconditions.checkNotNull(authToken.getPassword());

        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        session.setAttribute("someKey", "aValue");
        //collect user principals and credentials in a gui specific manner
        //such as username/password html form, X509 certificate, OpenID, etc.
        //We'll use the username/password example here since it is the most common.
        UsernamePasswordToken token = new UsernamePasswordToken(authToken.getLogin(), authToken.getPassword());

        //this is all you have to do to support 'remember me' (no config - built in!):
        token.setRememberMe(true);
        try {
            currentUser.login(token);
            //if no exception, that's it, we're done!
            //update permission
        } catch (UnknownAccountException uae) {
            //username wasn't in the system, show them an error message?
            return DTOErrorHelper.makeError(new UserManagerDTOBuilder().build(), "ERR_WRONG_LOGIN_OR_PASSWORD");
        } catch (IncorrectCredentialsException ice) {
            //password didn't match, try again?
            return DTOErrorHelper.makeError(new UserManagerDTOBuilder().build(), "ERR_WRONG_LOGIN_OR_PASSWORD");
        } catch (LockedAccountException lae) {
            //account for that username is locked - can't login.  Show them a message?
            return DTOErrorHelper.makeError(new UserManagerDTOBuilder().build(), "ERR_WRONG_LOGIN_OR_PASSWORD");
        } catch (AuthenticationException ae) {
            //unexpected condition - error?
            return DTOErrorHelper.makeError(new UserManagerDTOBuilder().build(), "ERR_WRONG_LOGIN_OR_PASSWORD");
        }
        UserManagerDTO userManagerDTO = new UserManagerDTOBuilder().withEntities(
                Arrays.asList(
                        new UserBuilder()
                                .withLogin(authToken.getLogin())
                                .withPassword(authToken.getPassword())
                                .build()
                )
        ).build();
        UserManagerDTO result = userLogic.search(userManagerDTO);
        if (result.getEntities().size() != 1) {
            return DTOErrorHelper.makeError(new UserManagerDTOBuilder().build(), "ERR_SERVER");
        } else {
            return result;
        }
    }

    public UserLogic getUserLogic() {
        return userLogic;
    }

    public void setUserLogic(UserLogic userLogic) {
        this.userLogic = userLogic;
    }

    @Override
    public UserManagerDTO delete(UserManagerDTO manageDTO) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Realm getRealm() {
        return realm;
    }

    public void setRealm(Realm realm) {
        this.realm = realm;
    }

}
