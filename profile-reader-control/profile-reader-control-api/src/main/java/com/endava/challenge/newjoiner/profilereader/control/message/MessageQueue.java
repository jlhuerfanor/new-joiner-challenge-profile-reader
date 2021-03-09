package com.endava.challenge.newjoiner.profilereader.control.message;

import com.endava.challenge.newjoiner.profilereader.model.domain.Profile;

public interface MessageQueue {
    Profile send(Profile value);
}
