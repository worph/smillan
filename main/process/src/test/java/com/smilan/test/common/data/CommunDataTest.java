package com.smilan.test.common.data;

import com.smilan.api.common.dto.builder.ContexteBuilder;
import com.smilan.api.common.dto.builder.EtatBuilder;
import java.time.OffsetDateTime;

/**
 * @author Thomas
 *
 */
public class CommunDataTest {

    public ContexteBuilder contexteBuilder() {
        return new ContexteBuilder()
                .withIdentifiantActeur("identifiantActeur")
                .withIdentifiantCorrelation("identifiantCorrelation")
                .withTimestamp(OffsetDateTime.now())
                .withApplication("application");
    }

    public ContexteBuilder contexteBuilder2() {
        return new ContexteBuilder()
                .withIdentifiantActeur(new StringBuilder("identifiantActeur").reverse().toString())
                .withIdentifiantCorrelation(new StringBuilder("identifiantCorrelation").reverse().toString())
                .withTimestamp(OffsetDateTime.now().minusDays(1))
                .withApplication(new StringBuilder("application").reverse().toString());
    }

    public EtatBuilder etatBuilder() {
        return new EtatBuilder()
                .withCode("code");
    }

    public EtatBuilder etatBuilder2() {
        return new EtatBuilder()
                .withCode(new StringBuilder("code").reverse().toString());
    }

}
