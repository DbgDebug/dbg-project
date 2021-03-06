package club.dbg.cms.blog.service.article;

import club.dbg.cms.blog.controller.ArticleController;
import org.junit.Test;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;

public class ArticleServiceTest {
    @Test
    public void test() {
        Class<?> a = ArticleController.class;
        Method[] methods = a.getMethods();
        for (Method method : methods) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            if (requestMapping == null) {
                continue;
            }
            System.out.println(method.getName().equals(""));
            if (requestMapping.method().length == 0) {
                continue;
            }
            System.out.println(requestMapping.method()[0].name());
        }

    }

    @Test
    public void bitTest() {
        System.out.println(2 >> 1);
    }
}