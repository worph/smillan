package com.smilan.support.crypto;

import java.security.Provider;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Thomas
 *
 */
@Configuration
public class JCEProviderConfiguration {

    @Bean
    public Provider provider() {
        return new BouncyCastleProvider();
    }
}
