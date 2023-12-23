package com.example.sbtdemo320.utils;

import java.util.Optional;
import java.util.function.Function;

public interface DistributeLockUtils {
    <T, R> void doExecute(String lockId, T param, Function<T, Optional<R>> function) throws RuntimeException;
}
