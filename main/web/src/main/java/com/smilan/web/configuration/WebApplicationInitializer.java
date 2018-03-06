package com.smilan.web.configuration;

import ch.qos.logback.ext.spring.web.LogbackConfigListener;
import java.util.ArrayList;
import java.util.Arrays;
import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * @author Thomas
 *
 */
public class WebApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    
    /**
     * @see org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer#getRootConfigClasses()
     */
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] {};
    }

    /**
     * @see org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer#getServletConfigClasses()
     */
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] { WebConfiguration.class };
    }

    /**
     * @see org.springframework.web.servlet.support.AbstractDispatcherServletInitializer#getServletMappings()
     */
    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }

    @Override
    protected Filter[] getServletFilters() {
        org.springframework.web.filter.DelegatingFilterProxy delegatingFilterProxy = new DelegatingFilterProxy("shiroFilter");
        delegatingFilterProxy.setTargetFilterLifecycle(true);
        ArrayList<Filter> filters = new ArrayList<>();
        filters.add(delegatingFilterProxy);
        return filters.toArray(new Filter[0]);
    }
    
    

    /**
     * @see org.springframework.web.servlet.support.AbstractDispatcherServletInitializer#onStartup(javax.servlet.ServletContext)
     */
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.addListener(new LogbackConfigListener());
        super.onStartup(servletContext);
    }

}
