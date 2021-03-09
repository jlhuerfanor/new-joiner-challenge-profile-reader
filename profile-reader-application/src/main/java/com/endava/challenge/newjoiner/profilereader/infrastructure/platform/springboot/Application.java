package com.endava.challenge.newjoiner.profilereader.infrastructure.platform.springboot;

import com.endava.challenge.newjoiner.profilereader.infrastructure.platform.config.PlatformConfigurationInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({
        "com.endava.challenge.newjoiner.profilereader.web.rest",
        "com.endava.challenge.newjoiner.profilereader.infrastructure",
})
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        var app = new SpringApplication(Application.class);
        app.addInitializers(new PlatformConfigurationInitializer());
        app.run(args);
    }
}
