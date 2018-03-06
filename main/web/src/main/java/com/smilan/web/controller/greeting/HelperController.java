package com.smilan.web.controller.greeting;

import com.smilan.api.domain.chat.ChatManager;
import com.smilan.process.commun.support.DummyPopulator;
import com.smilan.web.controller.APIVersion;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = APIVersion.apiPath + "helper", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class HelperController {

    private final AtomicLong counter = new AtomicLong();
    
    @Autowired
    DummyPopulator dummyPopulator;
    
    @Autowired
    private ChatManager chatManager;


    @GetMapping(path = "/greeting")
    public String greeting() {
        SecurityUtils.getSubject().checkRole("admin");
        return "Hello";
    }
    
    @GetMapping(path = "/populate")
    public String populate() {
        SecurityUtils.getSubject().checkRole("admin");
        dummyPopulator.populateDummy();
        return "Finished";
    }
        
    @GetMapping(path = "/clearChatUsers/{prefix}")
    public List<String> clearChatUsers(@PathVariable String prefix) {
        SecurityUtils.getSubject().checkRole("admin");
        return chatManager.cleanUsers(prefix);
    }
}
