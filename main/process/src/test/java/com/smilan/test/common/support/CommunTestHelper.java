package com.smilan.test.common.support;

import com.smilan.api.common.dto.Contexte;
import com.smilan.api.common.dto.Etat;
import org.junit.Assert;

/**
 * @author Thomas
 *
 */
public class CommunTestHelper {

    public void assertContexte(Contexte expectedContexte, Contexte contexte) {
        Assert.assertNotNull(contexte);
        Assert.assertEquals(expectedContexte.getIdentifiantActeur(), contexte.getIdentifiantActeur());
        Assert.assertEquals(expectedContexte.getIdentifiantCorrelation(), contexte.getIdentifiantCorrelation());
        Assert.assertEquals(expectedContexte.getTimestamp(), contexte.getTimestamp());
    }

    public void assertEtat(Etat expectedEtat, Etat etat) {
        Assert.assertNotNull(etat);
        Assert.assertEquals(expectedEtat.getCode(), etat.getCode());
        if (expectedEtat.getIdentifiant() != null) {
            Assert.assertEquals(expectedEtat.getIdentifiant(), etat.getIdentifiant());
        } else {
            Assert.assertNotNull(etat.getIdentifiant());
        }
        Assert.assertTrue(expectedEtat.getContexte() != null == (etat.getContexte() != null));
        if (etat.getContexte() != null) {
            this.assertContexte(expectedEtat.getContexte(), etat.getContexte());
        }
    }
}
