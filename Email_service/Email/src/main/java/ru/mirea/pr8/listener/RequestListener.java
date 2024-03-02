package ru.mirea.pr8.listener;

import io.opentracing.Span;
import io.opentracing.Tracer;
import jakarta.mail.MessagingException;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.mirea.pr8.email.EmailService;


@Service
@Log4j
public class RequestListener {

    @Autowired
    private Tracer tracer;
    @Autowired
    private EmailService emailService;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "req";
    @Autowired
    public RequestListener(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    @KafkaListener(topics = TOPIC, groupId = "mirea")
    public void listen(String email) throws MessagingException {
        log.info("TOPIC req | http://kafka-broker.default:9092 | listen method");
        Span span = tracer.buildSpan("email").start();

        emailService.sendMessage(email);

        span.finish();
    }

}
