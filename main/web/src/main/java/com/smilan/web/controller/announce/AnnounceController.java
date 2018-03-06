package com.smilan.web.controller.announce;

import com.smilan.api.domain.announce.AnnounceManager;
import com.smilan.api.domain.announce.AnnounceManagerDTO;
import com.smilan.api.domain.announce.builder.AnnounceBuilder;
import com.smilan.api.domain.announce.builder.AnnounceManagerDTOBuilder;
import com.smilan.api.domain.announce.dto.GeoSearchParameters;
import com.smilan.api.domain.announce.dto.GeoSearchResult;
import com.smilan.process.domain.security.RESTApiAuthenticator;
import com.smilan.web.controller.APIVersion;
import java.util.Arrays;
import java.util.List;
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
@RequestMapping(path = APIVersion.apiPath + "announces", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AnnounceController {

    @Autowired
    private AnnounceManager announceManager;
    
    @Autowired
    RESTApiAuthenticator restApiAuthenticator;

    @PostMapping(path = "/search", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public AnnounceManagerDTO search(@RequestBody AnnounceManagerDTO params,@RequestParam(required=false) String apikey) {
        restApiAuthenticator.login(apikey);
        return this.announceManager.search(params);
    }
    
    @PostMapping(path = "/delete", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public AnnounceManagerDTO delete(@RequestBody AnnounceManagerDTO params,@RequestParam(required=false) String apikey) {
        restApiAuthenticator.login(apikey);
        return this.announceManager.delete(params);
    }
    
    @PostMapping(path = "/geosearch", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public GeoSearchResult search(@RequestBody GeoSearchParameters params,@RequestParam(required=false) String apikey) {
        restApiAuthenticator.login(apikey);
        return announceManager.geoSearch(params);
    }
    
    @PostMapping(path = "/idsearch", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<String> searchId(@RequestBody AnnounceManagerDTO params,@RequestParam(required=false) String apikey) {
        restApiAuthenticator.login(apikey);
        return announceManager.idSearch(params);
    }
    
    @PostMapping(path = "/cleanup", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public void cleanUp(@RequestParam(required=false) String apikey) {
        restApiAuthenticator.login(apikey);
        announceManager.cleanUpAnnounces();
    }

    /*REST API*/
    /**
     * GET /tickets/12 - Retrieves a specific ticket
     *
     * @param identifiant
     * @return
     */
    @GetMapping(path = "/{identifiant}")
    @ResponseBody
    public AnnounceManagerDTO retreives(@PathVariable String identifiant,@RequestParam(required=false) String apikey) {
        restApiAuthenticator.login(apikey);
        return this.announceManager.search(new AnnounceManagerDTOBuilder()
                .withEntities(Arrays.asList(new AnnounceBuilder()
                        .withId(identifiant)
                        .build()))
                .build());
    }

    /**
     * POST /tickets - Creates/update/delete a new ticket
     *
     * @param announces
     * @param apikey
     * @return
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public AnnounceManagerDTO define(@RequestBody AnnounceManagerDTO announces,@RequestParam(required=false) String apikey) {
        restApiAuthenticator.login(apikey);
        return this.announceManager.define(announces);
    }



    /**
     * @return the gererCompteRenduActivite
     */
    public AnnounceManager getGererCompteRenduActivite() {
        return this.announceManager;
    }

    /**
     * @param gererCompteRenduActivite the gererCompteRenduActivite to set
     */
    public void setGererCompteRenduActivite(AnnounceManager gererCompteRenduActivite) {
        this.announceManager = gererCompteRenduActivite;
    }

}
