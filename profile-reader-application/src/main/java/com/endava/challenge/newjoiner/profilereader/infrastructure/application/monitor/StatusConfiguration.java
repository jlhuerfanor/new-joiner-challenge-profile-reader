package com.endava.challenge.newjoiner.profilereader.infrastructure.application.monitor;

import com.endava.challenge.newjoiner.profilereader.business.monitor.StatusBusiness;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StatusConfiguration {

    @Bean
    public StatusBusiness statusBusiness() {
        return new StatusBusiness();
    }
}
