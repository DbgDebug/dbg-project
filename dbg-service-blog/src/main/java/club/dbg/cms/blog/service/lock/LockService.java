package club.dbg.cms.blog.service.lock;

import java.util.concurrent.locks.ReentrantLock;

public interface LockService {
    ReentrantLock defaultLock(String key);

    ReentrantLock accountLock(Integer accountId);

    ReentrantLock orderLock(Integer orderId);

    Integer accountLockObject(Integer accountId);

    Integer orderLockObject(Integer orderId);
    // todo
    void accountLockDistribute(Integer accountId);
    // todo
    void accountUnlockDistribute(Integer accountId);
    // todo
    void orderLockDistribute(Integer accountId);
    // todo
    void orderUnlockDistribute(Integer accountId);
}
