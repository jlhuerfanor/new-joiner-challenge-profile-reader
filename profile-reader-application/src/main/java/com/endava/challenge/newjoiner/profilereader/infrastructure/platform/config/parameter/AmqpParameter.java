package com.endava.challenge.newjoiner.profilereader.infrastructure.platform.config.parameter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class AmqpParameter {
    private String uri;
    private String username;
    private String password;
    private String topicExchangeName;
    private String queueName;
    private String defaultTopic;
}
