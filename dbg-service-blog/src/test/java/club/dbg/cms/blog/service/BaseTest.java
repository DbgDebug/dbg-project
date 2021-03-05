package club.dbg.cms.blog.service;

import club.dbg.cms.blog.aop.LockMode;

public class BaseTest {
    public void test() {
        LockMode lockMode1 = LockMode.ACCOUNT_LOCK;
        LockMode lockMode2 = LockMode.ACCOUNT_LOCK;
    }
}
