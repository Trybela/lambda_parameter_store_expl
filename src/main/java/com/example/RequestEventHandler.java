package com.example;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.function.aws.MicronautRequestHandler;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@Setter
@Introspected
public class RequestEventHandler  extends MicronautRequestHandler<Map<String, String>, Void> {

    @Override
    public Void execute(Map<String, String> input) {
        log.info("LAMBDA successfully started! ", input);
        return null;
    }
}
