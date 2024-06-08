package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.support.MessageBuilder;

import java.util.function.Consumer;

@SpringBootApplication
public class SpringCloudStreamRabbitmqDelayMessageApplication {

    @Autowired
    private StreamBridge streamBridge;

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudStreamRabbitmqDelayMessageApplication.class, args);
    }

    @EventListener
    public void handle(ApplicationReadyEvent event) {
        System.out.println("Sending message to delay queue...");
        var message = MessageBuilder.withPayload(new SampleMessage("Hello from delay queue!")).build();
        streamBridge.send("initialTopic-in-0", message);
    }

    @Bean
    public Consumer<String> finalTopic() {
        return message -> {
            System.out.println("Received message in finalTopic queue: " + message);
        };
    }

}

class SampleMessage {

    private final String message;

    public SampleMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}