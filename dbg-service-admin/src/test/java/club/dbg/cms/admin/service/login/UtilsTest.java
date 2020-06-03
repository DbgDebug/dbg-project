package club.dbg.cms.admin.service.login;

import club.dbg.cms.admin.annotation.UseCases;
import club.dbg.cms.util.BCrypt;
import club.dbg.cms.util.PackageUtils;
import club.dbg.cms.util.ZLibUtils;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.text.StringEscapeUtils;
import org.junit.Test;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// @RunWith(SpringJUnit4ClassRunner.class)
public class UtilsTest {

    @Test
    public void byteTest() {
        int a = 1;
        int b = a & 0x3;
        System.out.println(b);
    }

    @Test
    public void bCryptTest() {
        String salt = BCrypt.gensalt();
        System.out.println(salt);
        System.out.println(BCrypt.hashpw("12345678", salt));
    }

    @Test
    public void emailTest() {
    }

    @Test
    public void htmlCodeTest() {
        System.out.println(StringEscapeUtils.unescapeHtml4(""));
    }

    @Test
    public void imagesTest() throws IOException {
        System.out.println(System.currentTimeMillis() / 1000);
        Thumbnails.of("D:\\work\\b.jpeg")
                .scale(1f)
                .outputQuality(0.9f)
                .toFile("D:\\work\\b3.jpeg");
        System.out.println(System.currentTimeMillis() / 1000);
    }

    @Test
    public void packageScanTest() throws ClassNotFoundException {
        String packageName = "club.dbg.cms.login.controller";
        List<String> classList = PackageUtils.getClassName(packageName, false);
        trackPermissionAnnotation(Class.forName(classList.get(0)));
    }

    @Test
    public void permissionAnnotationTest() {
    }

    public void trackPermissionAnnotation(Class<?> cl) {
        for (Method m : cl.getDeclaredMethods()) {
            RequestMapping requestMapping = m.getAnnotation(RequestMapping.class);
            System.out.println(m.getName());
            String[] values = requestMapping.value();
            for (String value : values) {
                System.out.println(value);
            }
            String[] a = requestMapping.params();
            System.out.println(a.length);
        }
    }


    @Test
    public void annotationTest() {
    }

    public static void trackUseCases(List<Integer> useCases, Class<?> cl) {
        for (Method m : cl.getDeclaredMethods()) {
            //获得注解的对象
            UseCases uc = m.getAnnotation(UseCases.class);
            if (uc != null) {
                System.out.println(m.getName());
                System.out.println("Found Use Case:" + uc.id() + " " + uc.description());
                useCases.remove(new Integer(uc.id()));
            }
        }
        for (int i : useCases) {
            System.out.println("Warning: Missing use case-" + i);
        }
    }

    @Test
    public void testa() {
        Map<String, String> concurrentHashMap = new ConcurrentHashMap<>();
        concurrentHashMap.put("/get_blog_list", "");
        concurrentHashMap.put("/get_blog_detail", "");
        concurrentHashMap.put("/get_comment_list", "");
        concurrentHashMap.put("/get_recent_comment", "");
        concurrentHashMap.put("/comment_blog", "");
        concurrentHashMap.put("/get_category_list", "");
        concurrentHashMap.put("/get_blog_by_category", "");

        if (concurrentHashMap.containsKey("/get_blog_list")) {
            System.out.println("ok");
        }
    }

    @Test
    public void t() {
        System.out.println("/blogfile/xix".indexOf("/blogfile"));
    }

}
