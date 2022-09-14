
package org.camunda.bpm.run;

import org.apache.catalina.filters.CorsFilter;
import org.camunda.bpm.engine.rest.security.auth.ProcessEngineAuthenticationFilter;
import org.camunda.bpm.run.property.BpmRunAuthenticationProperties;
import org.camunda.bpm.run.property.BpmRunProperties;
import org.camunda.bpm.spring.boot.starter.CamundaBpmAutoConfiguration;
import org.camunda.bpm.spring.boot.starter.rest.CamundaBpmRestInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.servlet.JerseyApplicationPath;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@EnableConfigurationProperties({BpmRunProperties.class})
@Configuration
@AutoConfigureAfter({CamundaBpmAutoConfiguration.class})
@ConditionalOnClass({CamundaBpmRestInitializer.class})
public class BpmRunRestConfiguration {
    @Autowired
    BpmRunProperties camundaBpmRunProperties;

    private static int CORS_FILTER_PRECEDENCE = 0;

    private static int AUTH_FILTER_PRECEDENCE = 1;

    @Bean
    @ConditionalOnProperty(name = {"enabled"}, havingValue = "true", prefix = "camunda.bpm.run.auth")
    public FilterRegistrationBean<Filter> processEngineAuthenticationFilter(JerseyApplicationPath applicationPath) {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean();
        registration.setName("camunda-auth");
        registration.setFilter((Filter) new ProcessEngineAuthenticationFilter());
        registration.setOrder(AUTH_FILTER_PRECEDENCE);
        String restApiPathPattern = applicationPath.getUrlMapping();
        registration.addUrlPatterns(new String[]{restApiPathPattern});
        BpmRunAuthenticationProperties properties = this.camundaBpmRunProperties.getAuth();
        if (properties.getAuthentication() == null || "basic".equals(properties.getAuthentication()))
            registration.addInitParameter("authentication-provider", "org.camunda.bpm.engine.rest.security.auth.impl.HttpBasicAuthenticationProvider");
        return registration;
    }

    @Bean
    @ConditionalOnProperty(name = {"enabled"}, havingValue = "true", prefix = "camunda.bpm.run.cors")
    public FilterRegistrationBean<Filter> corsFilter(JerseyApplicationPath applicationPath) {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean();
        registration.setName("camunda-cors");
        CorsFilter corsFilter = new CorsFilter();
        registration.setFilter((Filter) corsFilter);
        registration.setOrder(CORS_FILTER_PRECEDENCE);
        String restApiPathPattern = applicationPath.getUrlMapping();
        registration.addUrlPatterns(new String[]{restApiPathPattern});
        registration.addInitParameter("cors.allowed.origins", this.camundaBpmRunProperties
                .getCors().getAllowedOrigins());
        registration.addInitParameter("cors.allowed.methods", "GET,POST,HEAD,OPTIONS,PUT,DELETE");
        registration.addInitParameter("cors.allowed.headers", this.camundaBpmRunProperties
                .getCors().getAllowedHeaders());
        registration.addInitParameter("cors.exposed.headers", this.camundaBpmRunProperties
                .getCors().getExposedHeaders());
        registration.addInitParameter("cors.support.credentials",
                String.valueOf(this.camundaBpmRunProperties.getCors().getAllowCredentials()));
        registration.addInitParameter("cors.preflight.maxage", this.camundaBpmRunProperties
                .getCors().getPreflightMaxAge());
        return registration;
    }
}
