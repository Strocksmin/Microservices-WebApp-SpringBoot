package ru.mirea.pr8.controller;

import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.tag.Tags;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mirea.pr8.dto.LoginDTO;
import ru.mirea.pr8.model.User;
import ru.mirea.pr8.repository.UserRepository;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/auth")
@Log4j
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private Tracer tracer;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user){
        log.info("POST | /register | http://auth.default:8080 | register method");
        Span span = tracer.buildSpan("register").start();
        Tags.HTTP_METHOD.set(span, "POST");
        Tags.HTTP_URL.set(span, "/auth/register");


        if (userRepository.findUserByLogin(user.getLogin()) == null){
            userRepository.save(user);
            span.finish();
            return ResponseEntity.ok("Register is successful.");
        }
        span.finish();
        return ResponseEntity.ok("This login already exists");

    }

    @PostMapping("/login")
    public ResponseEntity<?> generateNewToken(@RequestBody LoginDTO loginDTO) {
        log.info("POST | /login | http://auth.default:8080 | login method");
        Span span = tracer.buildSpan("login").start();
        Tags.HTTP_METHOD.set(span, "POST");
        Tags.HTTP_URL.set(span, "/auth/login");


        User user = userRepository.findUserByLogin(loginDTO.getLogin());
        if (user == null){
            span.finish();
            return ResponseEntity.ok("User with this login doesnt exist");
        }
        if (loginDTO.getPassword().equals(user.getPassword())){
            UUID uuid = UUID.randomUUID();
            String token = uuid.toString().replaceAll("-","");

            // Сохраняем токен в Redis с ключом, например, по логину пользователя
            redisTemplate.opsForValue().set(user.getLogin(), token, 45, TimeUnit.MINUTES); // ключ-логин значение-токен живет 45 минут

            span.finish();
            return ResponseEntity.ok(token);
        }
        span.finish();
        return ResponseEntity.ok("Wrong password.");
    }

}
