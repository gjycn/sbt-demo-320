package com.example.sbtdemo320.utils.impl;

import com.example.sbtdemo320.utils.DistributeLockUtils;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Optional;
import java.util.function.Function;

@Slf4j
@Component
public class RedissonDistributeLockUtils implements DistributeLockUtils {
    private final RedissonClient redissonClient;
    private final TransactionTemplate transactionTemplate;

    public RedissonDistributeLockUtils(RedissonClient redissonClient, TransactionTemplate transactionTemplate) {
        this.redissonClient = redissonClient;
        this.transactionTemplate = transactionTemplate;
    }

    @Override
    public <T, R> void doExecute(String lockId, T param, Function<T, Optional<R>> function) throws RuntimeException {
        RLock lock = redissonClient.getLock(lockId);
        try {
            lock.lock();
            transactionTemplate.execute(transactionStatus -> {
                try {
                    return function.apply(param);
                } catch (Exception e) {
                    transactionStatus.setRollbackOnly();
                    log.error(e.getMessage(), e);
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
