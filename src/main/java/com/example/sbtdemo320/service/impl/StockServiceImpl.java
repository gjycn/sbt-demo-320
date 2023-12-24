package com.example.sbtdemo320.service.impl;

import com.example.sbtdemo320.model.Stock;
import com.example.sbtdemo320.repository.StockRepository;
import com.example.sbtdemo320.service.StockService;
import com.example.sbtdemo320.utils.DistributeLockUtils;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Optional;

@Slf4j
@Service
public class StockServiceImpl implements StockService {

    private final RedissonClient redissonClient;
    private final StockRepository stockRepository;
    private final TransactionTemplate transactionTemplate;
    private final DistributeLockUtils redissonDistributeLockUtils;

    public StockServiceImpl(RedissonClient redissonClient, StockRepository stockRepository, TransactionTemplate transactionTemplate, DistributeLockUtils redissonDistributeLockUtils) {
        this.redissonClient = redissonClient;
        this.stockRepository = stockRepository;
        this.transactionTemplate = transactionTemplate;
        this.redissonDistributeLockUtils = redissonDistributeLockUtils;
    }

    @Override
    public void checkAndLock1() {
        transactionTemplate.execute(transactionStatus -> {
            try {
                Optional<Stock> optionalStock = stockRepository.findById(1L);
                if (optionalStock.isPresent()) {
                    Stock stock = optionalStock.get();
                    if (stock.getCount() > 0) {
                        stock.setCount(stock.getCount() - 1);
                        stockRepository.save(stock);
                    }
                } else {
                    throw new RuntimeException("未找到该库存");
                }
            } catch (Exception e) {
                transactionStatus.setRollbackOnly();
                log.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
            return null;
        });
    }

    @Override
    public void checkAndLock2() {
        Long id = 1L;
        RLock lock = redissonClient.getLock(STR."stock_lock\{id}");
        try {
            lock.lock();
            transactionTemplate.execute(transactionStatus -> {
                try {
                    Optional<Stock> optionalStock = stockRepository.findById(id);
                    if (optionalStock.isPresent()) {
                        Stock stock = optionalStock.get();
                        if (stock.getCount() > 0) {
                            stock.setCount(stock.getCount() - 1);
                            stockRepository.save(stock);
                        }
                    } else {
                        throw new RuntimeException("未找到该库存");
                    }
                } catch (Exception e) {
                    transactionStatus.setRollbackOnly();
                    log.error(e.getMessage(), e);
                    throw new RuntimeException(e);
                }
                return null;
            });
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void checkAndLock3() {
        redissonDistributeLockUtils.doExecute("lock_1L", 1L, (id) -> {
            Optional<Stock> optionalStock = stockRepository.findById(id);
            if (optionalStock.isPresent()) {
                Stock stock = optionalStock.get();
                if (stock.getCount() > 0) {
                    stock.setCount(stock.getCount() - 1);
                    stockRepository.save(stock);
                }
            } else {
                throw new RuntimeException("未找到该库存");
            }
            return Optional.<Void>empty();
        });
    }


}
