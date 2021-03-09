package com.endava.challenge.newjoiner.profilereader.control.message;

import com.endava.challenge.newjoiner.profilereader.model.domain.Profile;
import com.google.gson.Gson;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.nio.charset.StandardCharsets;

public class RabbitMessageQueue implements MessageQueue {
    private final RabbitTemplate rabbitTemplate;
    private final Gson gson;

    public RabbitMessageQueue(RabbitTemplate rabbitTemplate, Gson gson) {
        this.rabbitTemplate = rabbitTemplate;
        this.gson = gson;
    }

    @Override
    public Profile send(Profile value) {
        this.rabbitTemplate.send(MessageBuilder.withBody(gson.toJson(value).getBytes(StandardCharsets.UTF_8))
                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .setType(Profile.class.getSimpleName())
                .build());
        return value;
    }
}
