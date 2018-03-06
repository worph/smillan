package com.smilan.web.controller.password;

import com.smilan.api.domain.user.User;
import com.smilan.api.domain.user.UserManager;
import com.smilan.process.domain.security.AdministrationExcutor;
import com.smilan.process.domain.security.RESTApiAuthenticator;
import com.smilan.web.controller.APIVersion;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.http.NameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.core.env.Environment;


@Controller
@RequestMapping(path = APIVersion.apiPath + "password", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PasswordController {
    
    @Autowired
    private RESTApiAuthenticator restApiAuthenticator;
        
    @Autowired
    private AdministrationExcutor administrationExcutor;
    
    @Autowired
    private UserManager userManager;
    
    @Autowired
    private Environment environment;
    
    
    public static final String PPT_HOSTNAME = "smillan.service.password.recovery.hostname";
    public static final String PPT_SECRETKEY = "smillan.service.password.recovery.secret.key";
    
    private String aes128Key;
    private AESCipherStringKey aesCipherStringKey;
    
    HashMap<String,OTP> storedOTPs = new HashMap<>();
    
    @PostConstruct
    void init(){
        aes128Key = environment.getProperty(PPT_SECRETKEY);
        aesCipherStringKey = new AESCipherStringKey(aes128Key);
    }
    
    private static class OTP{
        String OTP;
        Date date;
        String email;

        public OTP(String OTP, Date date, String email) {
            this.OTP = OTP;
            this.date = date;
            this.email = email;
        }
        
    }
    
    @GetMapping(path = "/forgotten")
    @ResponseBody
    public Object forgotten(@RequestParam String email, @RequestParam(required = false) String apikey) {
        restApiAuthenticator.login(apikey);
        
        HttpClient client = HttpClientBuilder.create().build();
        try {
            
            final User user = userManager.getUserFromUsername(email);
            if(user==null){
                return mkStatus("ERR_UNKOWN_USER");
            }
            String otp = RandomStringUtils.randomAlphanumeric(128);
            final String encryptedOTP = aesCipherStringKey.encrypt("otp/"+otp);
            //HttpGet httpget = new HttpGet("http://media.smillan.com/password/password_recovery.php?otp="+encryptedOTP+"&email="+email);
            HttpPost httpPost = new HttpPost("http://media.smillan.com/password/password_recovery.php");
            List<NameValuePair> params = new ArrayList<>(2);
            params.add(new BasicNameValuePair("otp", encryptedOTP));
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("server", environment.getProperty(PPT_HOSTNAME)));
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

            httpPost.addHeader("User-Agent", "Smillan API server");
            
            // Create a response handler
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            
            String responseBody = client.execute(httpPost, responseHandler);
            storedOTPs.put(otp, new OTP(otp, new Date(), email));
            if(responseBody.equalsIgnoreCase("OK")){
                return mkStatus("OK");
            }else{
                System.err.println("mail : "+responseBody);
                return mkStatus("ERR_MAIL_SERVER");
            }
        } catch (IOException ex) {
            Logger.getLogger(PasswordController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mkStatus("ERR_SERVER");
    }
    
    private Map<String,String> mkStatus(String info){
        Map<String,String> ret = new HashMap<>();
        ret.put("status", info);
        return ret;
    }
    
    @GetMapping(path = "/change")
    @ResponseBody
    public Object changeForgottenPasswordWithOTP(@RequestParam final String newPassword,@RequestParam String otp, @RequestParam(required = false) String apikey) {
        restApiAuthenticator.login(apikey);
        final OTP removedOtp = storedOTPs.remove(otp);
        if(removedOtp!=null){
            //administrator shall grant you your request
            final Subject subject = SecurityUtils.getSubject();
            subject.login(administrationExcutor.getAdminAuthToken());//TODO use admin run
            userManager.changePassword(removedOtp.email, newPassword);
            return mkStatus("OK");
        }else{
            return mkStatus("ERR_INVALID_OTP");
        }
    }

}
