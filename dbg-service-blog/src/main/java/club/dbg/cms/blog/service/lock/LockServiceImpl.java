package club.dbg.cms.blog.service.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class LockServiceImpl implements LockService {
    private final static Logger log = LoggerFactory.getLogger(LockServiceImpl.class);

    private final int ACCOUNT_LOCK_NUM = 100;

    private final int ORDER_LOCK_NUM = 100;

    private final Map<Integer, ReentrantLock> accountLockMap = new HashMap<>();

    private final Map<Integer, ReentrantLock> orderLockMap = new HashMap<>();

    // 数量由使用默认锁的方法数量决定
    private final Map<String, ReentrantLock> defaultLockMap = new ConcurrentHashMap<>();

    public LockServiceImpl() {
        for(int i = 0; i < ACCOUNT_LOCK_NUM; i++) {
            accountLockMap.put(i, new ReentrantLock());
        }
        for(int i = 0; i < ORDER_LOCK_NUM; i++) {
            orderLockMap.put(i, new ReentrantLock());
        }
    }

    @Override
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

    @Override
    public ReentrantLock accountLock(Integer accountId) {
        return accountLockMap.get(accountId % ACCOUNT_LOCK_NUM);
    }

    @Override
    public ReentrantLock orderLock(Integer orderId) {
        return orderLockMap.get(orderId % ORDER_LOCK_NUM);
    }

    @Override
    public Integer accountLockObject(Integer accountId) {
        return 0;
    }

    @Override
    public Integer orderLockObject(Integer orderId) {
        return 0;
    }

    @Override
    public void accountLockDistribute(Integer accountId) {

    }

    @Override
    public void accountUnlockDistribute(Integer accountId) {

    }

    @Override
    public void orderLockDistribute(Integer accountId) {

    }

    @Override
    public void orderUnlockDistribute(Integer accountId) {

    }
}
