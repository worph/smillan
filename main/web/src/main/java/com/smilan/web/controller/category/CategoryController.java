package com.smilan.web.controller.category;

import com.smilan.process.domain.category.CategoryToken;
import com.smilan.process.domain.category.CategoryAuthResult;
import com.smilan.api.domain.category.CategoryManager;
import com.smilan.api.domain.category.CategoryManagerDTO;
import com.smilan.api.domain.category.builder.CategoryBuilder;
import com.smilan.api.domain.category.builder.CategoryManagerDTOBuilder;
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
@RequestMapping(path = APIVersion.apiPath + "categories", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class CategoryController {

    @Autowired
    private CategoryManager categoryManager;    
    
    @Autowired
    RESTApiAuthenticator restApiAuthenticator;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public CategoryManagerDTO define(@RequestBody CategoryManagerDTO gererCompteRenduActiviteDTO,@RequestParam(required=false) String apikey) {
        restApiAuthenticator.login(apikey);
        return this.categoryManager.define(gererCompteRenduActiviteDTO);
    }

    @PostMapping(path = "/search", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public CategoryManagerDTO search(@RequestBody CategoryManagerDTO gererCompteRenduActiviteDTO,@RequestParam(required=false) String apikey) {
        restApiAuthenticator.login(apikey);
        return this.categoryManager.search(gererCompteRenduActiviteDTO);
    }
    
    @PostMapping(path = "/auth", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public CategoryAuthResult search(@RequestBody CategoryToken categoryToken,@RequestParam(required=false) String apikey) {
        restApiAuthenticator.login(apikey);
        return this.categoryManager.auth(categoryToken);
    }
    
    @GetMapping(path = "/")
    @ResponseBody
    /**
     * get a list of all the categories
     */
    public CategoryManagerDTO search(@RequestParam(required=false) String apikey) {
        return this.categoryManager.list();
    }
    
    @GetMapping(path = "/{identifiant}")
    @ResponseBody
    public CategoryManagerDTO search(@PathVariable String identifiant,@RequestParam(required=false) String apikey) {
        restApiAuthenticator.login(apikey);
        return this.categoryManager.search(new CategoryManagerDTOBuilder()
                .withEntities(Arrays.asList(new CategoryBuilder()
                        .withValue(identifiant)
                        .build()))
                .build());
    }

    public CategoryManager getCategoryManager() {
        return categoryManager;
    }

    public void setCategoryManager(CategoryManager categoryManager) {
        this.categoryManager = categoryManager;
    }

}
