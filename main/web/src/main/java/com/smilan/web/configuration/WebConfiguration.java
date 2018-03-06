package com.smilan.web.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.smilan.process.commun.configuration.CommunProcessConfiguration;
import com.smilan.process.commun.configuration.ProcessConfiguration;
import com.smilan.support.CommunSupportConfiguration;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.smilan")
@Import({
    WebBean.class, 
    CommunSupportConfiguration.class, 
    CommunProcessConfiguration.class, 
    ProcessConfiguration.class,
    ShiroSecurity.class
})
@ImportResource("classpath*:/META-INF/com/smillan/web/*.xml")
@PropertySource({"classpath:com/smilan/conf/main.properties"})//TODO why it is not working with PropertiesConfigurationPropertySource?
@EnableScheduling
public class WebConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    private ContextePopulatorInterceptor contextePopulatorInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/resources/**").allowedOrigins("*");
        registry.addMapping("/api/**").allowedOrigins("*");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
        registry.addResourceHandler("/password/**").addResourceLocations("/password/");
    }

    /**
     * @see
     * org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter#configureViewResolvers(org.springframework.web.servlet.config.annotation.ViewResolverRegistry)
     */
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.jsp();
        MappingJackson2JsonView mappingJackson2JsonView = new MappingJackson2JsonView();
        mappingJackson2JsonView.setExtractValueFromSingleKeyModel(true);
        registry.enableContentNegotiation(true, mappingJackson2JsonView);
    }

    /**
     * @see
     * org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter#addInterceptors(org.springframework.web.servlet.config.annotation.InterceptorRegistry)
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addWebRequestInterceptor(contextePopulatorInterceptor);
    }

    /**
     * @see
     * org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter#extendMessageConverters(java.util.List)
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (HttpMessageConverter<?> httpMessageConverter : converters) {
            if (httpMessageConverter instanceof MappingJackson2HttpMessageConverter) {
                MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = (MappingJackson2HttpMessageConverter) httpMessageConverter;
                mappingJackson2HttpMessageConverter.getObjectMapper()
                        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                        .disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
                return;
            }
        }
    }

    public ContextePopulatorInterceptor getContextePopulatorInterceptor() {
        return contextePopulatorInterceptor;
    }

    public void setContextePopulatorInterceptor(ContextePopulatorInterceptor contextePopulatorInterceptor) {
        this.contextePopulatorInterceptor = contextePopulatorInterceptor;
    }

}
