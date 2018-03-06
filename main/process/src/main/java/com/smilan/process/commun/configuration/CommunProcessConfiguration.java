package com.smilan.process.commun.configuration;

import com.smilan.logic.common.configuration.CommunLogicConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Thomas
 *
 */
@Configuration
@Import({ CommunLogicConfiguration.class })
public class CommunProcessConfiguration {

}
