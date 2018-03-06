package com.smilan.web.controller.mail;

import com.owlike.genson.Genson;
import com.smilan.process.domain.security.RESTApiAuthenticator;
import com.smilan.web.controller.APIVersion;
import com.smilan.web.controller.mail.builder.VerifyEmailResponceBuilder;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequestMapping(path = APIVersion.apiPath + "email", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class MailController {

    @Autowired
    RESTApiAuthenticator restApiAuthenticator;

    @GetMapping(path = "/verify")
    @ResponseBody
    public Object verify(@RequestParam String email, @RequestParam(required = false) String apikey) {
        restApiAuthenticator.login(apikey);
        
        HttpClient client = HttpClientBuilder.create().build();
        try {
            
            String apiKey = "02c9ab15e39c864e22eda5d214932bbd9cee9b24c7f7be0ce9d584935119";
            HttpGet httpget = new HttpGet("https://api.quickemailverification.com/v1/verify?email="+email+"&apikey="+apiKey);

            httpget.addHeader("User-Agent", "Smillan API server");
            
            // Create a response handler
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            
            String responseBody = client.execute(httpget, responseHandler);

            // Body contains your json stirng
            System.out.println("----------------------------------------");
            System.out.println(responseBody);
            System.out.println("----------------------------------------");
            final Map<String,Object> deserialize = new Genson().deserialize(responseBody,Map.class);
            return deserialize;
        } catch (IOException ex) {
            Logger.getLogger(MailController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}
