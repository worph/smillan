/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.logic.domain.chat;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import rocks.xmpp.addr.Jid;
import rocks.xmpp.core.XmppException;
import rocks.xmpp.core.session.TcpConnectionConfiguration;
import rocks.xmpp.core.session.XmppClient;
import rocks.xmpp.core.stanza.model.IQ;
import rocks.xmpp.extensions.data.model.DataForm;
import rocks.xmpp.extensions.httpbind.BoshConnectionConfiguration;
import rocks.xmpp.extensions.muc.ChatRoom;
import rocks.xmpp.extensions.muc.ChatService;
import rocks.xmpp.extensions.muc.MultiUserChatManager;
import rocks.xmpp.extensions.muc.model.RoomConfiguration;

/**
 *
 * @author pierr
 */
public class XMPPService {
    
    private XmppClient xmppClient = null;
    public static final String PPT_HOSTNAME = "smillan.service.chat.hostname";
    public static final String PPT_HOSTNAME_XML_RPC = "smillan.service.chat.hostname.xmlrpc";
    public static final String PPT_HOSTNAME_HTTP_BIND = "smillan.service.chat.hostname.httpbind";
    public static final String PPT_ADMIN_NAME = "smillan.service.chat.admin.name";
    public static final String PPT_ADMIN_PASSWORD = "smillan.service.chat.admin.password";
    public static final String PPT_PREFIX = "smillan.service.chat.prefix";
    public static final String PPT_SIMULATE_BEHAVIOUR = "smillan.service.chat.simulate";
    private final Properties property;
    private final String hostnamehttpbind;
    private final String adminName;
    private final String adminPassword;
    private final String prefix;
    private final boolean simulate;
    
    public XMPPService(Properties property) {
        this.property = property;
        hostnamehttpbind = property.getProperty(PPT_HOSTNAME_HTTP_BIND);
        adminName = property.getProperty(PPT_ADMIN_NAME);
        adminPassword = property.getProperty(PPT_ADMIN_PASSWORD);
        simulate = Boolean.parseBoolean(property.getProperty(PPT_SIMULATE_BEHAVIOUR,"false"));
        prefix = property.getProperty(PPT_PREFIX);
    }
    
    private synchronized void connect(){
        if(simulate){
            return;
        }
        if(xmppClient!=null){
            if(xmppClient.isConnected()){
                return;
            }
        }
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    
                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                    
                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
            };
            
            // Install the all-trusting trust manager
            SSLContext sc = null;
            try {
                sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            } catch (KeyManagementException ex) {
                System.err.println(ex);
            } catch (NoSuchAlgorithmException ex) {
                System.err.println(ex);
            }
            
            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            /*TcpConnectionConfiguration tcpConfiguration = TcpConnectionConfiguration.builder()
                    .hostname(hostname)
                    .port(5222)
                    .sslContext(sc)
                    .hostnameVerifier(allHostsValid)
                    .build();*/
            BoshConnectionConfiguration boshConfiguration80 = BoshConnectionConfiguration.builder()
                    .hostname(hostnamehttpbind)
                    .port(443)
                    .path("/http-bind")
                    .secure(true)//if true port is 443 otherwise 80
                    .build();
            /*BoshConnectionConfiguration boshConfiguration5280 = BoshConnectionConfiguration.builder()
                    .hostname(hostname)
                    .port(5280)
                    .path("/http-bind")
                    .build();*/
            xmppClient = XmppClient.create(hostnamehttpbind, boshConfiguration80);
            MultiUserChatManager multiUserChatManager = xmppClient.getManager(MultiUserChatManager.class);
            
            xmppClient.connect();
            
            xmppClient.login(adminName, adminPassword, "default");
            
            System.out.println("XMPP server connected");
            
        } catch (XmppException ex) {
            System.err.println(ex);
        }

    }
    
    public String createRoom(String name){
        if(simulate){
            return "simulate@conference.xmpp.com";
        }
        connect();
        try {            
            MultiUserChatManager multiUserChatManager = xmppClient.getManager(MultiUserChatManager.class);
            ChatService chatService = multiUserChatManager.createChatService(Jid.of("conference."+hostnamehttpbind));            
            ChatRoom chatRoom = chatService.createRoom(prefix+"-"+name);
            final DataForm result = chatRoom.getConfigurationForm().getResult();//create the room
            /*final DataForm.Field persistent = result.findField("muc#roomconfig_persistentroom");
            ArrayList<DataForm.Field> fields = new ArrayList<>(result.getFields());
            fields.remove(persistent);
            fields.add(DataForm.Field.builder().var(persistent.getVar()).value(true).build());
            DataForm dataForm = new DataForm(DataForm.Type.FORM, fields);*/
            final RoomConfiguration roomConf = RoomConfiguration.builder().persistent(true).build();
            final IQ result2 = chatRoom.configure(roomConf).getResult();
            return chatRoom.getAddress().toString();
        } catch (XmppException ex) {
            Logger.getLogger(XMPPService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
}
