package com.example.tokenization.config;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpServerConfig {
    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> servletContainerCustomizer() {
        return factory -> factory.addAdditionalTomcatConnectors(createStandardConnector());
    }

    private org.apache.catalina.connector.Connector createStandardConnector() {
        org.apache.catalina.connector.Connector connector =
                new org.apache.catalina.connector.Connector(org.apache.coyote.http11.Http11NioProtocol.class.getName());
        connector.setPort(8080);
        return connector;
    }
}
