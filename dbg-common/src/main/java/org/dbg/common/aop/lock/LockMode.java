package org.dbg.common.aop.lock;

public enum LockMode {
    DEFAULT_LOCK(1, "默认锁，按方法名加锁"),
    ACCOUNT_LOCK(2, "账号锁，按账号id加锁"),
    ORDER_LOCK(3, "订单锁，按订单id加锁");
    private final Integer value;
    private final String instruction;

    LockMode(Integer value, String instruction) {
        this.value = value;
        this.instruction = instruction;
    }

    public Integer value() {
        return value;
    }

    public String instruction() {
        return instruction;
    }
}
