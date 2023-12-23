package com.example.sbtdemo320.controller;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.lang.management.ManagementFactory;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.StructuredTaskScope;
import java.util.stream.Stream;

@Slf4j
@RestController
@RequestMapping("/api")
public class HelloController {

    private final RedissonClient redissonClient;

    public HelloController(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
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

        RLock lock = redissonClient.getLock("lock");
        try {
            lock.lock();

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            lock.unlock();
        }

        return "Hello world";
    }
}
