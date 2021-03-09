package com.endava.challenge.newjoiner.profilereader.control.message;

import com.endava.challenge.newjoiner.profilereader.model.domain.Profile;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class RabbitMessageQueueTest {


    private Gson gson;
    private RabbitTemplate rabbitTemplate;
    private RabbitMessageQueue rabbitMessageQueue;
    private Profile profile;
    private Message messageSent;
    private Profile response;
    private byte[] profileBytes;

    @BeforeEach
    void setup() {
        this.gson = new Gson();
        this.rabbitTemplate = Mockito.mock(RabbitTemplate.class);
        this.rabbitMessageQueue = new RabbitMessageQueue(rabbitTemplate, gson);

        Mockito.doAnswer(this::captureMessage)
                .when(this.rabbitTemplate)
                .send(Mockito.any());
    }

    private Object captureMessage(InvocationOnMock invocationOnMock) {
        this.messageSent = invocationOnMock.getArgument(0);
        return null;
    }

    @Test
    void send() {
        givenAProfileToSend();
        whenWeSendTheProfileToMessageQueue();
        thenAnExpectedMessageIsSent();
    }

    private void givenAProfileToSend() {
        this.profile = Profile.builder()
                .filename("")
                .idNumber(new BigInteger("1023456789"))
                .firstName("John")
                .lastName("Smith")
                .stack("Duno")
                .role("Developer")
                .englishLevel("B1")
                .domainExperience("2bdfined")
                .build();
        this.profileBytes = this.gson.toJson(this.profile).getBytes(StandardCharsets.UTF_8);
    }

    private void whenWeSendTheProfileToMessageQueue() {
        this.response = this.rabbitMessageQueue.send(this.profile);
    }

    private void thenAnExpectedMessageIsSent() {
        assertEquals(this.profile, this.response);
        assertNotNull(this.messageSent);
        assertTrue(Arrays.equals(this.profileBytes, this.messageSent.getBody()));
        assertEquals(MessageProperties.CONTENT_TYPE_JSON, this.messageSent.getMessageProperties().getContentType());
        assertEquals(Profile.class.getSimpleName(), this.messageSent.getMessageProperties().getType());
    }
}