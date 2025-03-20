package com.infoworks.lab.webapp.config;

import com.infoworks.lab.domain.entities.Username;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

@Configuration
//@EnableElasticsearchAuditing
@EnableElasticsearchRepositories(basePackages = {"com.infoworks.lab.domain.repositories"})
@PropertySource("classpath:application-elastic.properties")
public class ElasticConfig /*extends AbstractElasticsearchConfiguration*/ {

    @Value("${elastic.db.host}") String host;
    @Value("${elastic.db.port}") String port;

    /*@Bean
    public RestHighLevelClient elasticsearchClient() {
        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(String.format("%s:%s", host, port))
                .build();
        return RestClients.create(clientConfiguration)
                .rest();
    }*/

    @Bean
    public AuditorAware<Username> auditor() {
        return () -> Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .map(UserDetails.class::cast)
                .map(u -> new Username(u.getUsername()));
    }

}
