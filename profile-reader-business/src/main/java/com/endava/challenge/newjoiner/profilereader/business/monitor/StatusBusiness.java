package com.endava.challenge.newjoiner.profilereader.business.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatusBusiness {
    private static final Logger log = LoggerFactory.getLogger(StatusBusiness.class);

    public void ping() {
        log.info("Ping: OK");
    }

}
