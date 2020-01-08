package com.revolut.util;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Stack;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@FunctionalInterface
public interface Operation {
    static void performOperationSafely(List<Long> accounts, @NotNull Operation operation) {
        // sorting accounts to avoid deadlocks
        var locks = accounts.stream()
                .sorted()
                .map(LockFactory::getLock)
                .peek(ReentrantLock::lock)
                .collect(Collectors.toCollection(Stack::new));
        try {
            operation.perform();
        } finally {
            locks.forEach(ReentrantLock::unlock);
        }
    }

    void perform();
}
