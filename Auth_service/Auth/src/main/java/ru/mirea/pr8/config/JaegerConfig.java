package ru.mirea.pr8.config;

import io.opentracing.Tracer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JaegerConfig {

    @Bean
    public Tracer jaegerTracer() {
        return new io.jaegertracing.Configuration("AuthService")
                .withSampler(new io.jaegertracing.Configuration.SamplerConfiguration().withType("const").withParam(1))
                .withReporter(new io.jaegertracing.Configuration.ReporterConfiguration()
                        .withLogSpans(true)
                        .withSender(new io.jaegertracing.Configuration.SenderConfiguration()
                                .withEndpoint("http://jaeger.default:14268/api/traces"))) // HTTP endpoint
                .getTracer();
    }
}
