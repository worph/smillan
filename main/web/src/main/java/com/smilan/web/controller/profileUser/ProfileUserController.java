package com.smilan.web.controller.profileUser;

import com.smilan.api.domain.profile.AnonymousUserGenerator;
import com.smilan.api.domain.profile.ProfileManager;
import com.smilan.api.domain.profile.ProfileManagerDTO;
import com.smilan.api.domain.profile.builder.ProfileBuilder;
import com.smilan.api.domain.profile.builder.ProfileManagerDTOBuilder;
import com.smilan.api.domain.user.User;
import com.smilan.api.domain.user.UserAuthToken;
import com.smilan.api.domain.user.UserManager;
import com.smilan.api.domain.user.UserManagerDTO;
import com.smilan.api.domain.user.UserUpgradePackage;
import com.smilan.process.domain.security.RESTApiAuthenticator;
import com.smilan.web.controller.APIVersion;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(path = APIVersion.apiPath + "profiles", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ProfileUserController {

    @Autowired
    private ProfileManager profileManager;
    
    @Autowired
    private UserManager userManager;
    
    @Autowired
    private AnonymousUserGenerator anonymousUserGenerator;

    @Autowired
    RESTApiAuthenticator restApiAuthenticator;
    
    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ProfileManagerDTO define(@RequestBody ProfileManagerDTO gererCompteRenduActiviteDTO,@RequestParam(required=false) String apikey) {
        restApiAuthenticator.login(apikey);
        return this.profileManager.define(gererCompteRenduActiviteDTO);
    }

    @PostMapping(path = "/search", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ProfileManagerDTO search(@RequestBody ProfileManagerDTO gererCompteRenduActiviteDTO,@RequestParam(required=false) String apikey) {
        restApiAuthenticator.login(apikey);
        return this.profileManager.search(gererCompteRenduActiviteDTO);
    }
    
    @PostMapping(path = "/createNewUser")
    @ResponseBody
    public UserManagerDTO createNewUser(@RequestParam(required=false) String apikey) {
        restApiAuthenticator.login(apikey);
        return anonymousUserGenerator.createNewUser();
    }
    
    @PostMapping(path = "/createNewProfile/{parentUserId}")
    @ResponseBody
    public ProfileManagerDTO createNewProfile(@PathVariable String parentUserId,@RequestParam(required=false) String apikey) {
        restApiAuthenticator.login(apikey);
        return anonymousUserGenerator.createNewProfile(parentUserId);
    }
    
    @PostMapping(path = "/upgrade", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public UserManagerDTO upgrade(@RequestBody UserUpgradePackage userUpgradePackage,@RequestParam(required=false) String apikey) {
        restApiAuthenticator.login(apikey);
        return anonymousUserGenerator.upgradeProfile(userUpgradePackage);
    }
    
    @PostMapping(path = "/auth", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public UserManagerDTO auth(@RequestBody UserAuthToken userAuthToken,@RequestParam(required=false) String apikey) {
        restApiAuthenticator.login(apikey);
        return userManager.authSearch(userAuthToken);
    }
    
    @PostMapping(path = "/change/credentials")
    @ResponseBody
    public User changeMail(@RequestBody UserAuthToken userAuthToken,@RequestParam(required=false) String apikey) {
        restApiAuthenticator.login(apikey);
        final User userFromApiKey = userManager.getUserFromApiKey(apikey);
        if(userAuthToken.getLogin()==null || "".equals(userAuthToken.getLogin())){
            userAuthToken.setLogin(userFromApiKey.getLogin());
        }
        if(userAuthToken.getPassword()==null || "".equals(userAuthToken.getPassword())){
            userAuthToken.setPassword(userFromApiKey.getPassword());
        }
        userManager.changeLoginPassword(userAuthToken.getLogin(),userAuthToken.getPassword(),userFromApiKey.getLogin(),userFromApiKey.getPassword());
        return userManager.getUserFromApiKey(apikey);
    }
        
    @GetMapping(path = "/{identifiant}")
    @ResponseBody
    public ProfileManagerDTO search(@PathVariable String identifiant,@RequestParam(required=false) String apikey) {
        restApiAuthenticator.login(apikey);
        return this.profileManager.search(new ProfileManagerDTOBuilder()
                .withEntities(Arrays.asList(new ProfileBuilder()
                        .withId(identifiant)
                        .build()))
                .build());
    }

}
