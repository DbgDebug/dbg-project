package club.dbg.cms.util;

import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

public class VideoIDTest {
    @Test
    public void idTest() throws InterruptedException {
        ThreadTest threadTest = new ThreadTest(true);
        Thread threadA = new Thread(threadTest, "A");
        Thread threadB = new Thread(threadTest, "B");
        threadA.start();
        threadB.start();
        Thread.sleep(2000);

    }

    @Test
    public void fileTest() throws IOException {
        byte[] bytes = FileUtils.readFileByBytes("E:/a.txt");
        String a = new String(bytes);
        System.out.println(a);
    }


}
