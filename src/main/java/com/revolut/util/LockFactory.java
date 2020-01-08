package com.revolut.util;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * An utility class to obtain locks for account operations. We want thread to
 * wait only if it's processing an account that is already being processed at
 * the same time, because of that a Map of locks is stored.
 * <p>
 * Additionally, to make sure we don't run out of memory if there are potentially
 * many accounts that make few operations, a {@link WeakHashMap} is used so that
 * unused locks are removed by garbage collector.
 */
public class LockFactory {
    private static Map<Long, ReentrantLock> lockMap = new WeakHashMap<>();

    /**
     * Returns an existing lock for an account or creates a new one if the account
     * is not processed at the moment. Since {@link WeakHashMap} does not provide
     * thread safety, the method is synchronized.
     *
     * @param accountId an id of an account that needs to be guarded by a lock.
     * @return a {@link ReentrantLock} that can be used to provide thread safety
     * for an account operation.
     */
    synchronized public static ReentrantLock getLock(long accountId) {
        return lockMap.computeIfAbsent(accountId, acc -> new ReentrantLock(true));
    }
}
