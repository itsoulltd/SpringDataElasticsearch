package com.infoworks.lab.webapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StartupConfig implements CommandLineRunner {

    @Value("${server.port}")
    private String serverPort;

    @Override
    public void run(String... args) throws Exception {
        //How to use executor:
        System.out.println(String.format("http://localhost:%s/swagger-ui/index.html", serverPort));
        //
    }

    @EventListener
    public void handleContextStartedListener(ContextRefreshedEvent event){
        System.out.println("ContextStarted....");
    }

    @EventListener
    public void handleContextStoppedListener(ContextClosedEvent event){
        System.out.println("ContextStopped....");
    }
}
