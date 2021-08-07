package club.dbg.cms.util;

import org.junit.Test;

import java.io.IOException;

public class VideoIDTest {
    @Test
    public void idTest() {
        System.out.println(UUIDUtils.getUUIDNotHyphen());
    }

    @Test
    public void fileTest() throws IOException {
        byte[] bytes = FileUtils.readFileByBytes("E:/a.txt");
        String a = new String(bytes);
        System.out.println(a);
    }
}
