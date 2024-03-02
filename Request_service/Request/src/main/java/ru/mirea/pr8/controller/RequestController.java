package ru.mirea.pr8.controller;

import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.tag.Tags;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.mirea.pr8.dto.RequestDTO;


@RestController
@Log4j
public class RequestController {

    @Autowired
    private Tracer tracer;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "req";
    @Autowired
    public RequestController(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    @PostMapping("/request")
    public ResponseEntity<?> takeRequest(@RequestBody RequestDTO requestDTO, @RequestHeader("Authorization") String authorizationHeader) {
        log.info("POST | /request | http://request.default:1111 | request method");
        Span span = tracer.buildSpan("request").start();
        Tags.HTTP_METHOD.set(span, "POST");
        Tags.HTTP_URL.set(span, "/request");

        String login = requestDTO.getLogin();

        // Извлечь токен из заголовка Authorization (пример: "Bearer <token>")
        String providedToken = extractBearerToken(authorizationHeader);

        // Извлечь сохраненный токен из Redis по ключу (логин)
        String storedToken = redisTemplate.opsForValue().get(login);

        if (storedToken != null && storedToken.equals(providedToken)) {
            // Токены совпадают, отправить электронное письмо в топик Kafka
            kafkaTemplate.send(TOPIC, requestDTO.getEmail());

            span.finish();
            return ResponseEntity.ok("Request is taken");
        } else {
            // Токены не совпадают, вернуть сообщение об ошибке
            span.finish();
            return ResponseEntity.ok("Token is not valid");
        }
    }

    // Метод для извлечения токена из заголовка Authorization
    private String extractBearerToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

}
