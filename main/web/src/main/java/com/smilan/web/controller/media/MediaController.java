package com.smilan.web.controller.media;

import com.smilan.api.domain.media.MediaManager;
import com.smilan.api.domain.media.MediaManagerDTO;
import com.smilan.api.domain.media.builder.MediaBuilder;
import com.smilan.api.domain.media.builder.MediaManagerDTOBuilder;
import com.smilan.process.domain.security.RESTApiAuthenticator;
import com.smilan.web.controller.APIVersion;
import java.util.Arrays;
import net.karneim.pojobuilder.GeneratePojoBuilder;
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
@GeneratePojoBuilder(withCopyMethod = true, intoPackage = "*.builder")
@RequestMapping(path = APIVersion.apiPath + "media", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class MediaController {

    @Autowired
    private MediaManager mediaManager;

    @Autowired
    RESTApiAuthenticator restApiAuthenticator;
    
    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public MediaManagerDTO define(@RequestBody MediaManagerDTO gererCompteRenduActiviteDTO,@RequestParam(required=false) String apikey) {
        restApiAuthenticator.login(apikey);
        return this.mediaManager.define(gererCompteRenduActiviteDTO);
    }

    @PostMapping(path = "/search", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public MediaManagerDTO search(@RequestBody MediaManagerDTO gererCompteRenduActiviteDTO,@RequestParam(required=false) String apikey) {
        restApiAuthenticator.login(apikey);
        return this.mediaManager.search(gererCompteRenduActiviteDTO);
    }

    @GetMapping(path = "/{identifiant}")
    @ResponseBody
    public MediaManagerDTO search(@PathVariable String identifiant,@RequestParam(required=false) String apikey) {
        restApiAuthenticator.login(apikey);
        return this.mediaManager.search(new MediaManagerDTOBuilder()
                .withEntities(Arrays.asList(new MediaBuilder()
                        .withId(identifiant)
                        .build()))
                .build());
    }

    public MediaManager getMediaManager() {
        return mediaManager;
    }

    public void setMediaManager(MediaManager mediaManager) {
        this.mediaManager = mediaManager;
    }

}
