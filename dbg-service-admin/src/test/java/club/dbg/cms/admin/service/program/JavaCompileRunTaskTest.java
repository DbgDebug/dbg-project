package club.dbg.cms.admin.service.program;

import club.dbg.cms.util.bilibili.JavaPatternUtils;
import org.junit.Test;

import java.net.URISyntaxException;
import java.util.regex.Matcher;

public class JavaCompileRunTaskTest {

    @Test
    public void javaNameTest() throws URISyntaxException {
        String s = "public class FileNamePatternUtils{\n";
        Matcher m = JavaPatternUtils.javaPublicClassName.matcher(s);
        if(!m.find()){
            return;
        }
        String className = m.group(1);
        System.out.println(className);
    }

}