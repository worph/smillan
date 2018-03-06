/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.logic.domain.security;

import com.smilan.api.domain.user.User;
import com.smilan.logic.domain.announce.AnnounceLogic;
import com.smilan.logic.domain.profile.ProfileLogic;
import com.smilan.logic.domain.user.UserLogic;
import com.smilan.logic.domain.user.entity.UserEntity;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;

/**
 *
 * @author pierr
 */
public class Realm extends AuthorizingRealm {

    public static final String PPT_ADMIN_USERNAME = "smillan.service.security.shiro.username";
    public static final String PPT_ADMIN_PASSWORD = "smillan.service.security.shiro.password";

    private final ProfileLogic profileLogic;
    private final UserLogic userLogic;
    private final AnnounceLogic announceLogic;

    private final String adminUsername;
    private final String adminPassword;
    private final UsernamePasswordToken authenticationToken;

    public final static String ROLE_ADMIN = "admin";
    public final static String ROLE_USER = "user";
    public final static String ROLE_USER_ANONYMOUS = "user-anonymous";
    public final static String ROLE_USER_UPGRADED = "user-upgraded";
    public final static String ROLE_GUEST = "guest";

    public Realm(ProfileLogic profileLogic, UserLogic userLogic, AnnounceLogic announceLogic, Properties property) {
        super();
        this.profileLogic = profileLogic;
        this.userLogic = userLogic;
        this.announceLogic = announceLogic;
        adminUsername = property.getProperty(PPT_ADMIN_USERNAME, "admin");
        adminPassword = property.getProperty(PPT_ADMIN_PASSWORD, "admin");
        authenticationToken = new UsernamePasswordToken(adminUsername, adminPassword);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws org.apache.shiro.authc.AuthenticationException {

        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String username = upToken.getUsername();
        String password = new String(upToken.getPassword());
        if (username.equals("guest")) {
            AuthenticationInfo info = new SimpleAuthenticationInfo(username, "guest".toCharArray(), getName());
            return info;
        }
        if (username.equals(adminUsername)) {
            AuthenticationInfo info = new SimpleAuthenticationInfo(username, adminPassword.toCharArray(), getName());
            return info;
        } else {
            final User userFromUsername = userLogic.getUserFromUsername(username);
            if (userFromUsername == null) {
                throw new UnknownAccountException("No account found for user [" + username + "]");
                //throw new IllegalArgumentException("wrong login or password");
            }

            //double check to be sure (1rst check is the login pasword geoSearch request)
            if (userFromUsername.getPassword().equals(password)) {
                //All is good
                AuthenticationInfo info = new SimpleAuthenticationInfo(username, password.toCharArray(), getName());
                return info;
            } else {
                throw new UnknownAccountException("Invalid password user [" + username + "]");
            }
        }
    }

    public void invalidateCurrentSubjectCache() {
        clearCache(SecurityUtils.getSubject().getPrincipals());
    }

    /**
     * To call when an announce is updated for example or user is upgraded
     *
     * @param principals
     */
    public void updateAuthorizationInfo(PrincipalCollection principals) {
        clearCachedAuthorizationInfo(principals);
    }

    private void setPermission(SimpleAuthorizationInfo info, List<String> permissions) {
        for (String permission : permissions) {
            info.addStringPermission(permission);
        }
    }

    private List<String> getPermissionFromRole(String role) {
        ArrayList<String> ret = new ArrayList<String>();
        switch (role) {
            case ROLE_USER_ANONYMOUS:
                //permission on ressource
                CrudPermission puser = new CrudPermission("user");
                ret.add(puser.permissionCreate());
                ret.add(puser.permissionRead("*", UserEntity.NAME_ANONYMOUS));

                CrudPermission pprofile = new CrudPermission("profile");
                ret.add(pprofile.permissionCreate());
                ret.add(pprofile.permissionRead("*", "*"));

                CrudPermission pannounce = new CrudPermission("announce");
                ret.add(pannounce.permissionCreate());
                //ret.add(pannounce.permissionRead("*", "*")); always authorized

                CrudPermission pmedia = new CrudPermission("media");
                ret.add(pmedia.permissionCreate());
                //ret.add(pmedia.permissionRead("*", "*")); always authorized

                CrudPermission pcategory = new CrudPermission("category");
                ret.add(pcategory.permissionCreate());
                //ret.add(pcategory.permissionRead("*", "*")); always authorized
                break;
            case ROLE_USER_UPGRADED:
                break;
            case ROLE_ADMIN:
                ret.add("*:*:*:*:*");//unlimited power
                break;
        }
        return ret;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        if (principals == null) {
            throw new AuthorizationException("PrincipalCollection method argument cannot be null.");
        }

        String username = (String) getAvailablePrincipal(principals);
        //System.out.println("Auth | username : " + username);

        Set<String> roleNames = new HashSet<>();
        if (username.equals(adminUsername)) {
            List<String> roles = new ArrayList<>();
            roles.add(ROLE_ADMIN);
            roles.add(ROLE_USER_ANONYMOUS);
            roles.add(ROLE_USER_UPGRADED);
            roles.add(ROLE_USER);
            roles.add(ROLE_GUEST);
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            //set user roles
            info.setRoles(new HashSet<>(roles));
            //compute permistion from role
            for (String role : roles) {
                setPermission(info, getPermissionFromRole(role));
            }
            return info;
        } else if (username.equals("guest")) {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roleNames);
            CrudPermission puser = new CrudPermission("user");
            info.addStringPermission(puser.permissionCreate());
            CrudPermission pprofile = new CrudPermission("profile");
            info.addStringPermission(pprofile.permissionCreate());
            CrudPermission pmedia = new CrudPermission("media");
            info.addStringPermission(pmedia.permissionCreate());
            return info;
        } else {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            final User user = userLogic.getUserFromUsername(username);
            if (user == null) {
                return info;
            }
            //compute user ROLES (get from database)
            final Set<String> roles = user.getRoles();

            roles.add(ROLE_USER_ANONYMOUS);

            if (!user.isAnonymous()) {
                roles.add(ROLE_USER_UPGRADED);
            }

            //set user roles
            info.setRoles(new HashSet<>(roles));

            //compute permistion from role
            for (String role : roles) {
                setPermission(info, getPermissionFromRole(role));
            }

            //add user specific permission stored in database (eg profile/announce/category permission)
            for (String permission : user.getPermissions()) {
                info.addStringPermission(permission);
            }

            return info;
        }
    }

    public void setPermissionForNewProfile(String profileID, String userId) {
        User user = getUserFromSecurityEnv();
        if(user==null){
            //user not present in database (eg admin) => we don't need to store permission
            //user creration is entry point so there is no cred for this operation
            user=userLogic.getUserFromUserId(userId);
            if(user==null){
                throw new AuthorizationException("Inconsistent data");
            }
        }
        //TODO this should be a list right?
        Long profileIDR = profileLogic.getIDFromUserID(Long.parseLong(user.getId()));
        if (!profileID.equals(profileIDR + "")) {
            throw new AuthorizationException("Inconsistent data");
        }
        //update user permission
        ArrayList<String> ret = new ArrayList<>();
        CrudPermission pprofile = new CrudPermission("profile");
        ret.add(pprofile.permissionUpdateDelete(profileID + "", "*"));
        user.getPermissions().addAll(ret);
        updateUserPermission(user);
    }

    public void setPermissionForNewUser(String userId) {
        User user = getUserFromSecurityEnv();
        if(user==null){
            //user not present in database (eg admin) => we don't need to store permission
            //user creration is entry point so there is no cred for this operation
            user=userLogic.getUserFromUserId(userId);
            if(user==null){
                throw new AuthorizationException("Inconsistent data");
            }
        }else{
            if (!userId.equals(user.getId())) {
                throw new AuthorizationException("Inconsistent data");
            }
        }
        //update user permission
        ArrayList<String> ret = new ArrayList<>();
        CrudPermission puser = new CrudPermission("user");
        ret.add(puser.permissionReadDelete(userId + "", "*"));
        //TODO remove all permission all operation on user should be made by ADMIN
        ret.add(puser.permissionUpdate(userId + "", UserEntity.NAME_ANONYMOUS+","+UserEntity.NAME_PASSWORD+","+UserEntity.NAME_EMAIL));
        if(user.getPermissions()==null){
            user.setPermissions(new HashSet<>());
        }
        if(user.getRoles()==null){
            user.setRoles(new HashSet<>());
        }
        user.getPermissions().addAll(ret);
        updateUserPermission(user);
    }

    public void setNewAnnouncePermissionForUser(String profileID, String aid) {
        User user = getUserFromSecurityEnv();
        if(user==null){
            //user not present in database (eg admin) => we don't need to store permission
            return;
        }
        //TODO this should be a list right?
        Long profileIDR = profileLogic.getIDFromUserID(Long.parseLong(user.getId()));
        if (!profileID.equals(profileIDR + "")) {
            throw new AuthorizationException("Inconsistent data");
        }
        //update user permission
        ArrayList<String> ret = new ArrayList<>();
        CrudPermission pannounce = new CrudPermission("announce");
        ret.add(pannounce.permissionUpdateDelete(aid + "", "*"));
        user.getPermissions().addAll(ret);
        updateUserPermission(user);
    }

    public void setNewCategoryAuthPermission(String category) {
        User user = getUserFromSecurityEnv();
        if(user==null){
            //user not present in database (eg admin) => we don't need to store permission
            return;
        }
        //update user permission
        ArrayList<String> ret = new ArrayList<>();
        CategoryPermision pannounce = new CategoryPermision(category);
        ret.add(pannounce.permissionAll());
        user.getPermissions().addAll(ret);
        updateUserPermission(user);
    }

    private User getUserFromSecurityEnv() {
        final PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
        String username = (String) getAvailablePrincipal(principals);
        User user = userLogic.getUserFromUsername(username);
        return user;
    }

    private void updateUserPermission(User user) {
        runAsAdministrator(()->{
            userLogic.set(user);//do this as administrator since user is not allowed to update his permission    
        });
        final PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
        clearCache(principals);
        clearCachedAuthorizationInfo(principals);
    }
    
    public void runAsAdministrator(Runnable runnable) {
        Subject subject;
        SecurityUtils.getSecurityManager();
        subject = SecurityUtils.getSubject();
        boolean runas = false;
        if (subject.isAuthenticated()) {
            runas = true;
        }
        if (runas) {
            final SimplePrincipalCollection simplePrincipalCollection = new SimplePrincipalCollection();
            simplePrincipalCollection.add(ROLE_ADMIN, this.getName());
            subject.runAs(simplePrincipalCollection);
        }else{
            subject.login(authenticationToken);
        }
        subject.execute(runnable);
        if (runas) {
            subject.releaseRunAs();
        }else{
            subject.logout();//go back to not authenticated
        }
    }
}
