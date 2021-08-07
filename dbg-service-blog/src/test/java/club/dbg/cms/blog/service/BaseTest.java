package club.dbg.cms.blog.service;

import org.dbg.common.aop.lock.LockMode;

public class BaseTest {
    public void test() {
        LockMode lockMode1 = LockMode.ACCOUNT_LOCK;
        LockMode lockMode2 = LockMode.ACCOUNT_LOCK;
    }
}
