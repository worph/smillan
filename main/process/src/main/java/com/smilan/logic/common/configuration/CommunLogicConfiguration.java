package com.smilan.logic.common.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Thomas
 *
 */
@Configuration
@Import({ CommunDatabaseConfiguration.class })
@EnableTransactionManagement
public class CommunLogicConfiguration {

}
