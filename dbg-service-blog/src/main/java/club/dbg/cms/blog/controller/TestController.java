package club.dbg.cms.blog.controller;

import club.dbg.cms.util.ResponseBuild;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/test")
public class TestController {
    @RequestMapping(value = "urls", method = RequestMethod.GET)
    public ResponseEntity<ResponseBuild<List<UrlMethod>>> getUrls(HttpServletRequest request) {
        List<UrlMethod> uList = new ArrayList<>();//存储所有url集合
        WebApplicationContext wac = (WebApplicationContext) request.getAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE);//获取上下文对象
        RequestMappingHandlerMapping bean = wac.getBean(RequestMappingHandlerMapping.class);//通过上下文对象获取RequestMappingHandlerMapping实例对象
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = bean.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> rmi : handlerMethods.entrySet()) {
            UrlMethod urlMethod = new UrlMethod();
            List<String> tmp = new ArrayList<>(rmi.getKey().getPatternsCondition().getPatterns());
            urlMethod.setUrl(tmp);
            urlMethod.setMethod(rmi.getValue().getMethod().getName());
            uList.add(urlMethod);
        }

        return ResponseBuild.build(uList);
    }

    static class UrlMethod {
        private List<String> url;
        private String method;

        public List<String> getUrl() {
            return url;
        }

        public void setUrl(List<String> url) {
            this.url = url;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }
    }
}
