/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.process.domain.announce.configuration;

import com.smilan.api.domain.announce.AnnounceManager;
import com.smilan.process.domain.security.AdministrationExcutor;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author pierr
 */
@Component
public class AnnounceCleanUpScheduledTask {
    @Autowired
    AnnounceManager announceManager;
    
    @Autowired
    AdministrationExcutor administrationExcutor;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(cron = "${smillan.service.announce.cleanup.delay.cron}")
    public void scheduledAnnounceCleanUp() {
        administrationExcutor.run(() -> {
            System.out.println("Clean up announces "+ dateFormat.format(new Date()));
            announceManager.cleanUpAnnounces();
        });
    }
}
