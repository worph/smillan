/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.process.domain.chat;

import com.smilan.logic.domain.chat.XMPPService;
import com.smilan.api.domain.chat.ChatManager;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.springframework.stereotype.Service;
import rocks.xmpp.addr.Jid;
import rocks.xmpp.core.XmppException;
import rocks.xmpp.core.session.TcpConnectionConfiguration;
import rocks.xmpp.core.session.XmppClient;
import rocks.xmpp.core.stanza.model.Message;
import rocks.xmpp.core.stanza.model.Presence;
import rocks.xmpp.extensions.httpbind.BoshConnectionConfiguration;
import rocks.xmpp.extensions.register.RegistrationManager;
import rocks.xmpp.extensions.register.model.Registration;
import rocks.xmpp.im.roster.RosterManager;
import rocks.xmpp.util.concurrent.AsyncResult;

/**
 *
 * @author pierr
 */
public class DefaultChatManager implements ChatManager {
    public static final String PPT_HOSTNAME_XML_RPC = XMPPService.PPT_HOSTNAME_XML_RPC;
    public static final String PPT_PREFIX = XMPPService.PPT_PREFIX;
    public static final String PPT_ADMIN_PASSWORD = XMPPService.PPT_ADMIN_PASSWORD;
    public static final String PPT_ADMIN_NAME = XMPPService.PPT_ADMIN_NAME;


    private final String hostname;//desktop-0sk5g6k / "86.242.159.251"/ 192.168.1.10 / 127.0.0.1 / chat.smillan.com
    private final String prefix;
    private final String adminPassword;
    private final String adminUser;
    private final Properties property;

    public DefaultChatManager(Properties property) {
        this.property = property;
        hostname = property.getProperty(PPT_HOSTNAME_XML_RPC);
        if(hostname==null){
            System.err.println("chat PPT_HOSTNAME null");
        }
        prefix = property.getProperty(PPT_PREFIX); 
        if(prefix==null){
            System.err.println("chat PPT_PREFIX null");
        }
        adminPassword = property.getProperty(PPT_ADMIN_PASSWORD); 
        if(adminPassword==null){
            System.err.println("chat PPT_ADMIN_PASSWORD null");
        }
        adminUser = property.getProperty(PPT_ADMIN_NAME); 
        if(adminUser==null){
            System.err.println("chat PPT_ADMIN_NAME null");
        }
    }
    
    @PostConstruct
    public void init() {
        //cleanUsers("debug");
    }
    
    @Override
    public String createUser(String user, String password) {
        user = prefix+"-"+user;
        createUserEjjaberdRegistration(user, password);
        //createUserInBandRegistration(user, password);
        System.out.println("user created");
        String chatId = user + "@" + hostname;
        return chatId;
    }

    public void createUserInBandRegistration(String user, String password) {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                }

                @Override
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
            throw new Error(ex);
        } catch (NoSuchAlgorithmException ex) {
            throw new Error(ex);
        }

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        TcpConnectionConfiguration tcpConfiguration = TcpConnectionConfiguration.builder()
                .hostname(hostname)
                .port(5222)
                .sslContext(sc)
                .hostnameVerifier(allHostsValid)
                .build();
        XmppClient xmppClient = XmppClient.create(hostname, tcpConfiguration);
        // Listen for presence changes
        xmppClient.addInboundPresenceListener(e -> {
            Presence presence = e.getPresence();
            // Handle inbound presence.
            System.out.println(presence);
        });
        // Listen for messages
        xmppClient.addInboundMessageListener(e -> {
            Message message = e.getMessage();
            // Handle inbound message.
            System.out.println(message);
        });
        // Listen for roster pushes
        xmppClient.getManager(RosterManager.class).addRosterListener(e -> {
            // Roster has changed
            System.out.println(e);
        });

        try {
            xmppClient.connect();
        } catch (XmppException e) {
            throw new Error(e);
        }
        final RegistrationManager manager = xmppClient.getManager(RegistrationManager.class);
        final Registration build = Registration.builder().email(user + "@smillan.com").username(user).password(password).build();
        final AsyncResult<Void> register = manager.register(build);//Registers a new account. Call this method before authenticating.
        try {
            register.getResult();
        } catch (XmppException ex) {
            throw new Error(ex);
        }
    }

    public void createUserEjjaberdRegistration(String user, String password) {
        //time limite for user creation
        try {
            //https://docs.ejabberd.im/developer/ejabberd-api/admin-api/
            XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
            config.setServerURL(new URL("http://" + hostname + ":4560"));//"http://127.0.0.1:5280/http-bind"
            //config.setBasicUserName(adminUser+ "@" + hostname);
            //config.setBasicPassword(adminPassword);

            XmlRpcClient client = new XmlRpcClient();
            client.setConfig(config);

            {
                final HashMap<String, Object> login = new HashMap<String, Object>() {
                    {
                        put("user", adminUser);
                        put("server", hostname);
                        put("password", adminPassword);
                        put("admin", true);
                    }
                };
                final HashMap<String, Object> data = new HashMap<String, Object>() {
                    {
                        put("host", hostname);
                        put("user", user);
                        put("password", password);
                    }
                };
                client.execute("register", new Object[]{login,data});
            }

        } catch (MalformedURLException ex) {
            Logger.getLogger(DefaultChatManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XmlRpcException ex) {
            Logger.getLogger(DefaultChatManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public ArrayList<String> cleanUsers(String prefix) {
        try {
            //https://docs.ejabberd.im/developer/ejabberd-api/admin-api/
            XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
            config.setServerURL(new URL("http://" + hostname + ":4560"));//"http://127.0.0.1:5280/http-bind"
            config.setBasicUserName(adminUser+ "@" + hostname);
            config.setBasicPassword(adminPassword);

            XmlRpcClient client = new XmlRpcClient();
            client.setConfig(config);
            
            /**
             * retreive users
             */
            ArrayList<String> usersList = new ArrayList<>();
            {
                final HashMap<String, Object> hashMap = new HashMap<String, Object>() {
                    {
                        put("host", hostname);
                    }
                };
                final Object execute = client.execute("registered_users", new Object[]{hashMap});
                HashMap<String, Object[]> users = (HashMap<String, Object[]>) execute;
                final Object[] get = users.get("users");
                for (Object obj : get) {
                    HashMap<String, String> hashMap1 = (HashMap<String, String>) obj;
                    final String username = hashMap1.get("username");
                    if (!"admin".equals(username)) {
                        if(username.startsWith(prefix)){
                            System.out.println(username);
                            usersList.add(username);
                        }
                    }
                }
            }
            /**
             * unregister all users
             */
            {
                for (String user : usersList) {

                    final HashMap<String, Object> hashMap = new HashMap<String, Object>() {
                        {
                            put("host", hostname);
                            put("user", user);
                        }
                    };
                    client.execute("unregister", new Object[]{hashMap});
                }
            }
            return usersList;
        } catch (MalformedURLException ex) {
            Logger.getLogger(DefaultChatManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XmlRpcException ex) {
            Logger.getLogger(DefaultChatManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        throw new Error();
    }

    @Override
    public void linkUsers(String userAJid, String userBJid) {        
        try {
            final String[] userAsplit = userAJid.split("@");
            if(!userAsplit[1].equals(hostname)){
                throw new Error();
            }
            final String[] userBsplit = userBJid.split("@");
            if(!userBsplit[1].equals(hostname)){
                throw new Error();
            }
            //https://docs.ejabberd.im/developer/ejabberd-api/admin-api/
            XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
            config.setServerURL(new URL("http://" + hostname + ":4560"));//"http://127.0.0.1:5280/http-bind"
            config.setBasicUserName(adminUser+ "@" + hostname);
            config.setBasicPassword(adminPassword);

            XmlRpcClient client = new XmlRpcClient();
            client.setConfig(config);

            {
                //add_rosteritem 
                final HashMap<String, Object> hashMap = new HashMap<String, Object>() {
                    {
                        put("localuser", userAsplit[0]);
                        put("localserver", hostname);
                        put("user", userBsplit[0]);
                        put("server", hostname);
                        put("nick", userBsplit[0]);
                        put("group", "Friends");
                        put("subs", "both");
                    }
                };
                client.execute("add_rosteritem", new Object[]{hashMap});
            }            
            {
                //add_rosteritem 
                final HashMap<String, Object> hashMap = new HashMap<String, Object>() {
                    {
                        put("localuser", userBsplit[0]);
                        put("localserver", hostname);
                        put("user", userAsplit[0]);
                        put("server", hostname);
                        put("nick", userAsplit[0]);
                        put("group", "Friends");
                        put("subs", "both");
                    }
                };
                client.execute("add_rosteritem", new Object[]{hashMap});
            }

        } catch (MalformedURLException ex) {
            Logger.getLogger(DefaultChatManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XmlRpcException ex) {
            Logger.getLogger(DefaultChatManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
