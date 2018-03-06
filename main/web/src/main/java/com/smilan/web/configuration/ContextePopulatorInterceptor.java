package com.smilan.web.configuration;

import com.smilan.api.common.dto.Contexte;
import net.karneim.pojobuilder.GeneratePojoBuilder;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

/**
 * @author Thomas
 *
 */
@GeneratePojoBuilder(withCopyMethod = true, intoPackage = "*.builder")
public class ContextePopulatorInterceptor implements WebRequestInterceptor {

    private Contexte contexte;

    /**
     * @see org.springframework.web.context.request.WebRequestInterceptor#preHandle(org.springframework.web.context.request.WebRequest)
     */
    @Override
    public void preHandle(WebRequest request) throws Exception {
        this.contexte.setIdentifiantActeur("identifiantActeur");
    }

    /**
     * @see org.springframework.web.context.request.WebRequestInterceptor#postHandle(org.springframework.web.context.request.WebRequest, org.springframework.ui.ModelMap)
     */
    @Override
    public void postHandle(WebRequest request, ModelMap model) throws Exception {
    }

    /**
     * @see org.springframework.web.context.request.WebRequestInterceptor#afterCompletion(org.springframework.web.context.request.WebRequest, java.lang.Exception)
     */
    @Override
    public void afterCompletion(WebRequest request, Exception ex) throws Exception {
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

}
