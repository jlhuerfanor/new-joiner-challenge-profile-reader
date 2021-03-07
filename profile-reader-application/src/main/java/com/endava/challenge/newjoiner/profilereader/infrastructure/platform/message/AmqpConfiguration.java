package com.endava.challenge.newjoiner.profilereader.infrastructure.platform.message;

import com.endava.challenge.newjoiner.profilereader.control.message.MessageQueue;
import com.endava.challenge.newjoiner.profilereader.control.message.RabbitMessageQueue;
import com.endava.challenge.newjoiner.profilereader.infrastructure.platform.config.parameter.AmqpParameter;
import com.endava.challenge.newjoiner.profilereader.model.domain.Profile;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

@Configuration
public class AmqpConfiguration {
    private static final Logger log = LoggerFactory.getLogger(AmqpConfiguration.class);
    private final AmqpParameter amqpParameter;
    private final Gson gson;

    @Autowired
    public AmqpConfiguration(AmqpParameter amqpParameter, Gson gson) {
        this.amqpParameter = amqpParameter;
        this.gson = gson;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();

        connectionFactory.setUri(amqpParameter.getUri());
        connectionFactory.setUsername(amqpParameter.getUsername());
        connectionFactory.setPassword(amqpParameter.getPassword());

        return connectionFactory;
    }

    @Bean
    public Queue profileQueue() {
        return new Queue(amqpParameter.getQueueName(), false);
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(amqpParameter.getTopicExchangeName());
    }

    @Bean
    public Binding binding(Queue profileQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(profileQueue)
                .to(topicExchange)
                .with(amqpParameter.getDefaultTopic());
    }

    @Bean
    @Primary
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        var template = new RabbitTemplate(connectionFactory);

        template.setExchange(amqpParameter.getTopicExchangeName());
        template.setRoutingKey(amqpParameter.getDefaultTopic());
        template.setDefaultReceiveQueue(amqpParameter.getQueueName());

        return template;
    }

    @Bean
    public MessageListenerContainer messageListenerContainer(
            ConnectionFactory connectionFactory,
            Queue profileQueue) {

        SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer(connectionFactory);
        listenerContainer.setMessageListener(this::loopbackListener);
        listenerContainer.setQueues(profileQueue);
        listenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        listenerContainer.start();

        return listenerContainer;
    }

    @Bean
    public MessageQueue messageQueue(RabbitTemplate template) {
        return new RabbitMessageQueue(template, gson);
    }

    private void loopbackListener(Message message) {
        if(message.getMessageProperties().getType().equals("Profile")) {
            var payload = gson.fromJson(new InputStreamReader(new ByteArrayInputStream(message.getBody())), Profile.class);
            log.info("Received: {}", payload);
        }
    }
}
