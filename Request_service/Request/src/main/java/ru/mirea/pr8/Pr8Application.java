package ru.mirea.pr8;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class Pr8Application {

    public static void main(String[] args) {
        SpringApplication.run(Pr8Application.class, args);
    }

}
