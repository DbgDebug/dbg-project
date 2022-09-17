package club.dbg.cms.util;

public class ThreadTest implements Runnable {
    private boolean isA;

    ThreadTest(boolean isA) {
        this.isA = isA;
    }

    @Override
    public void run() {
        if(isA) {
            isA = false;
            aTest();
        } else {
            bTest();
        }
    }

    private synchronized void aTest() {
        System.out.println(Thread.currentThread().getName());
        System.out.println("aTest");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private synchronized void bTest() {
        System.out.println(Thread.currentThread().getName());
        System.out.println("bTest");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
