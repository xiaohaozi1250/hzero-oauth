package org.hzero.oauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;

import org.hzero.oauth.security.config.SecurityProperties;
import org.hzero.oauth.security.custom.CustomClientDetailsService;
import org.hzero.oauth.security.resource.ResourceMatcher;

/**
 * @author wuguokai
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private SecurityProperties properties;
    @Autowired(required = false)
    private ResourceMatcher resourceMatcher;
    @Autowired
    private CustomClientDetailsService clientDetailsService;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.setSharedObject(ClientDetailsService.class, clientDetailsService);
        if (properties.isCustomResourceMatcher() && resourceMatcher != null) {
            http
                .requestMatcher(resourceMatcher)
                .authorizeRequests()
                .anyRequest().authenticated();
        } else {
            http
                .antMatcher("/api/**")
                .authorizeRequests()
                .anyRequest().authenticated();
        }
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("default");
    }
}
