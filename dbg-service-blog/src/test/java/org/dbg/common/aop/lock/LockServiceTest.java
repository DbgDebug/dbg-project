package org.dbg.common.aop.lock;

import club.dbg.cms.upload.exception.BlogException;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class LockServiceTest {
    private final int LOCK_NUM = 2;

    private final ReentrantLock defaultLock = new ReentrantLock();

    private final Map<Integer, ReentrantLock> accountLockMap = new HashMap<>();

    public LockServiceTest() {
        for (int i = 0; i < LOCK_NUM; i++) {
            ReentrantLock accountLock = new ReentrantLock();
            accountLockMap.put(i, accountLock);
        }
    }

    @Test
    public void lockExceptionTest() {
        defaultLock.lock();
        synchronized (ThreadTest.class) {
            try {
                System.out.println("a");
                if (LOCK_NUM == 2) {
                    throw new BlogException("");
                }
            } catch (Exception e) {
                throw e;
            } finally {
                defaultLock.unlock();
                System.out.println("unlock");
            }
        }
    }


    @Test
    public void lockTest() throws InterruptedException {
        int n = 10000000;
        ThreadTest threadTest = new ThreadTest(n);
        ThreadTest threadTestB = new ThreadTest(n);
        Thread threadA = new Thread(threadTest, "A");
        Thread threadB = new Thread(threadTest, "B");
        Thread threadC = new Thread(threadTest, "C");
        Thread threadD = new Thread(threadTest, "D");
        Thread threadE = new Thread(threadTest, "E");
        Thread thread1 = new Thread(threadTestB, "1");
        Thread thread2 = new Thread(threadTestB, "2");
        Thread thread3 = new Thread(threadTestB, "3");
        Thread thread4 = new Thread(threadTestB, "4");
        Thread thread5 = new Thread(threadTestB, "5");
        threadA.start();
        threadB.start();
        threadC.start();
        threadD.start();
        threadE.start();
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
        Thread.sleep(5000);
        System.out.println("A:" + threadTest.getA());
        System.out.println("B:" + threadTestB.getA());
    }
}

class ThreadTest implements Runnable {
    private static final ReentrantLock reentrantLock = new ReentrantLock();
    private Integer a = 0;
    private final int n;

    public ThreadTest(int n) {
        this.n = n;
    }

    @Override
    public void run() {
        long timestamp = System.currentTimeMillis();
        reentrantLock.lock();
        if (n == 0) {
            throw new BlogException("");
        }
        synchronized (ThreadTest.class) {
            try {
                for (int i = 0; i < n; i++) {
                    a++;
            /*
            synchronized (this) {
                a++;
            }*/
                    // System.out.println(a);
                }
            } finally {
                reentrantLock.unlock();
            }
        }
        System.out.println(Thread.currentThread().getName() + ":" + (System.currentTimeMillis() - timestamp));
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }
}