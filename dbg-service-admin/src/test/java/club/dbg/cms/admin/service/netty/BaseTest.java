package club.dbg.cms.admin.service.netty;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timer;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class BaseTest {
    @Test
    public void timerTest() throws InterruptedException {
        Timer timer = new HashedWheelTimer();
        timer.newTimeout(timeout -> {
            System.out.println(Thread.currentThread().getName());
        }, 5, TimeUnit.SECONDS);
        Timer timers = new HashedWheelTimer();
        timers.newTimeout(timeout -> {
            System.out.println(Thread.currentThread().getName());
        }, 8, TimeUnit.SECONDS);
        int i = 0;
        while(i < 3){
            i++;
            Thread.sleep(5000);
        }
    }
}
