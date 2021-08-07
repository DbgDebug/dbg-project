package club.dbg.cms.upload.service.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestServiceImplTest {

    @Autowired
    TestService testService;

    @Test
    public void testVoid() throws InterruptedException {
        Thread thread1 = new Thread(new ThreadTest(testService));
        Thread thread2 = new Thread(new ThreadTest(testService));
        Thread thread3 = new Thread(new ThreadTest(testService));
        Thread thread4 = new Thread(new ThreadTest(testService));
        Thread thread5 = new Thread(new ThreadTest(testService));
        Thread thread6 = new Thread(new ThreadTest(testService));
        Thread thread7 = new Thread(new ThreadTest(testService));
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
        thread6.start();
        thread7.start();
        Thread.sleep(10000);
        System.out.println(testService.getN());
    }

    @Test
    public void test() {
        System.out.println(testService.test());
    }

    @Test
    public void test2() {
        System.out.println(testService.test("哈哈"));
    }
}

class ThreadTest implements Runnable {
    TestService testService;

    ThreadTest(TestService testService) {
        this.testService = testService;
    }

    @Override
    public void run() {
        for(int i = 0; i < 1000; i++) {
            testService.testVoid("");
        }
    }
}