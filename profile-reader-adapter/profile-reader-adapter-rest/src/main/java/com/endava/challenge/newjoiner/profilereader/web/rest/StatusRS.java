package com.endava.challenge.newjoiner.profilereader.web.rest;

import com.endava.challenge.newjoiner.profilereader.business.monitor.StatusBusiness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/monitor")
public class StatusRS {
    @Autowired
    public StatusBusiness statusBusiness;

    @GetMapping("/ping")
    public void ping() {
        statusBusiness.ping();
    }

}
