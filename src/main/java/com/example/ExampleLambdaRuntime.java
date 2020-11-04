package com.example;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import io.micronaut.function.aws.runtime.AbstractMicronautLambdaRuntime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.MalformedURLException;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class ExampleLambdaRuntime extends AbstractMicronautLambdaRuntime<Map<String, String>,
        Void, Map<String, String>, Void> {

    public static void main(String[] args) {
        new ExampleLambdaRuntime().run(args);
    }

    @Override
    public void run(String... args) {
        try {
            super.run(args);
        } catch (MalformedURLException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    protected RequestHandler<Map<String, String>, Void> createRequestHandler(String... args) {
        return new RequestEventHandler();
    }
}
