package com.smilan.support;

import com.smilan.api.common.dto.Contexte;
import com.smilan.api.common.dto.Etat;
import com.smilan.api.common.support.CommunHelper;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.List;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * @author Thomas
 *
 */
@GeneratePojoBuilder(withCopyMethod = true, intoPackage = "*.builder")
public class ContexteService {

    private Contexte           contexte;

    private final CommunHelper communHelper = new CommunHelper();

    private Clock              clock;

    /**
     * @return the context timestamp or the system timestamp
     */
    public OffsetDateTime now() {
        if (this.contexte != null && this.contexte.getTimestamp() != null) {
            return this.contexte.getTimestamp();
        } else {
            return OffsetDateTime.now(this.clock);
        }
    }

    /**
     * @return the system timestamp
     */
    public OffsetDateTime systemNow() {
        return OffsetDateTime.now(this.clock);
    }

    public void consoliderEtat(Etat etat) {
        if (etat.getContexte() == null) {
            etat.setContexte(this.communHelper.copy(this.contexte));
            if (etat.getContexte().getTimestamp() == null) {
                etat.getContexte().setTimestamp(this.now());
            }
        }
    }

    public void consoliderEtats(List<Etat> etats) {
        for (Etat etat : etats) {
            this.consoliderEtat(etat);
        }
    }

    /**
     * @return the contexte
     */
    public Contexte getContexte() {
        return this.contexte;
    }

    /**
     * @param contexte the contexte to set
     */
    public void setContexte(Contexte contexte) {
        this.contexte = contexte;
    }

    public Clock getClock() {
        return this.clock;
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }
}
