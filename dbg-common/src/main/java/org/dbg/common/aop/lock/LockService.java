package org.dbg.common.aop.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class LockService {
    private final static Logger log = LoggerFactory.getLogger(LockService.class);

    private final int ACCOUNT_LOCK_NUM = 100;

    private final int ORDER_LOCK_NUM = 100;

    private final Map<Integer, ReentrantLock> accountLockMap = new HashMap<>();

    private final Map<Integer, ReentrantLock> orderLockMap = new HashMap<>();

    // 数量由使用默认锁的方法数量决定
    private final Map<String, ReentrantLock> defaultLockMap = new ConcurrentHashMap<>();

    private static volatile LockService lockService = null;

    public static LockService getInstance() {
        if (lockService == null) {
            synchronized (LockService.class) {
                if (lockService == null) {
                    lockService = new LockService();
                    return lockService;
                }
            }
        }
        return lockService;
    }

    private LockService() {
        for(int i = 0; i < ACCOUNT_LOCK_NUM; i++) {
            accountLockMap.put(i, new ReentrantLock());
        }
        for(int i = 0; i < ORDER_LOCK_NUM; i++) {
            orderLockMap.put(i, new ReentrantLock());
        }
    }

    public ReentrantLock defaultLock(String key) {
        if(!defaultLockMap.containsKey(key)){
            synchronized (this) {
                if(!defaultLockMap.containsKey(key)){
                    defaultLockMap.put(key, new ReentrantLock());
                }
            }
        }
        return defaultLockMap.get(key);
    }

    public ReentrantLock accountLock(Integer accountId) {
        return accountLockMap.get(accountId % ACCOUNT_LOCK_NUM);
    }

    public ReentrantLock orderLock(Integer orderId) {
        return orderLockMap.get(orderId % ORDER_LOCK_NUM);
    }

    public Integer accountLockObject(Integer accountId) {
        return 0;
    }

    public Integer orderLockObject(Integer orderId) {
        return 0;
    }

    public void accountLockDistribute(Integer accountId) {

    }

    public void accountUnlockDistribute(Integer accountId) {

    }

    public void orderLockDistribute(Integer accountId) {

    }

    public void orderUnlockDistribute(Integer accountId) {

    }
}
