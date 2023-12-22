package com.example.sbtdemo320;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.lang.management.ManagementFactory;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.StructuredTaskScope;
import java.util.stream.Stream;

@Slf4j
@RestController
@SpringBootApplication
public class SbtDemo320Application {

    public static void main(String[] args) {
        SpringApplication.run(SbtDemo320Application.class, args);
    }

    @GetMapping("/hello")
    public String hello() {
        log.info(STR."外面: \{Thread.currentThread()}");

        String res = CompletableFuture.supplyAsync(() -> {
            log.info(STR."CompletableFuture.runAsync: \{Thread.currentThread()}");
            return "1";
        }).join();

        Flux.just(1, 2, 3)
                .handle((var, _) -> log.info(STR."Flux: \{var} \{Thread.currentThread()}"))
                .subscribe();

        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            scope.fork(() -> {
                log.info(STR."StructuredTaskScope.ShutdownOnFailure1: \{Thread.currentThread()}");
                return null;
            });
            scope.fork(() -> {
                log.info(STR."StructuredTaskScope.ShutdownOnFailure2: \{Thread.currentThread()}");
                return null;
            });
            scope.fork(() -> {
                log.info(STR."StructuredTaskScope.ShutdownOnFailure3: \{Thread.currentThread()}");
                return null;
            });

            scope.join().throwIfFailed();
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }

        Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9)
                .parallel()
                .forEach(item -> log.info(STR."ParallelStream: \{item} \{Thread.currentThread()}"));

        ManagementFactory.getGarbageCollectorMXBeans()
                .parallelStream()
                .forEach(item -> log.info(item.getName()));


        return "Hello world";
    }
}
