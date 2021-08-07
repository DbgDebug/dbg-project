package club.dbg.cms.admin.controller;

import club.dbg.cms.admin.dao.RoleMapper;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * @author dbg
 */
@RestController
public class TestController {
    private final static Logger log = LoggerFactory.getLogger(TestController.class);

    private Integer count = 0;

    private final RoleMapper roleMapper;

    private final WebApplicationContext applicationContext;

    public TestController(WebApplicationContext applicationContext, RoleMapper roleMapper) {
        this.applicationContext = applicationContext;
        this.roleMapper = roleMapper;
    }

    @RequestMapping(value = "test", method = RequestMethod.GET)
    public ResponseEntity<String> test() {
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> urlMap = mapping.getHandlerMethods();
        List<Map<String, String>> list = new ArrayList<>();
        for(Map.Entry<RequestMappingInfo, HandlerMethod> m : urlMap.entrySet()){
            Map<String, String> map = new HashMap<>();
            RequestMappingInfo requestMappingInfo = m.getKey();
            HandlerMethod handlerMethod = m.getValue();
            PatternsRequestCondition p = requestMappingInfo.getPatternsCondition();
            for (String url : Objects.requireNonNull(p).getPatterns()) {
                map.put("url", url);
            }
            map.put("className", handlerMethod.getMethod().getDeclaringClass().getName());
            map.put("method", handlerMethod.getMethod().getName());
            RequestMethodsRequestCondition methodsCondition = requestMappingInfo.getMethodsCondition();
            for (RequestMethod requestMethod : methodsCondition.getMethods()) {
                map.put("type", requestMethod.toString());
            }
            list.add(map);
        }
        return ResponseEntity.ok(JSON.toJSONString(list));
    }

    @RequestMapping(value = "jmeter", method = RequestMethod.GET)
    public void jmeter(HttpServletRequest request, HttpServletResponse response,
                       @RequestParam("name") String name) throws IOException {
        ServletOutputStream out = null;
        FileInputStream in = null;
        try {
            //in = new FileInputStream(new File("D:\\DevelopmentEnvironment\\nginx-1.14.2\\html\\" + name));
            in = new FileInputStream("D:\\Project\\JAVA\\dbg-project\\dbg-camera\\ws.h264");
            //设置文件头：最后一个参数是设置下载文件名
            //response.setHeader("Content-Disposition", "attachment;fileName="+fileName);
            out = response.getOutputStream();
            // 读取文件流
            int len = 0;
            byte[] buffer = new byte[1024 * 10];
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
        } catch (FileNotFoundException e) {
            log.error("responseFileStream error:FileNotFoundException" + e.toString());
        } catch (Exception e) {
            log.error("responseFileStream error:" + e.toString());
        } finally {
            try {
                assert out != null;
                out.close();
                in.close();
            } catch (NullPointerException e) {
                log.error("responseFileStream stream close() error:NullPointerException" + e.toString());
            } catch (Exception e) {
                log.error("responseFileStream stream close() error:" + e.toString());
            }
        }
    }

    @RequestMapping(value = "con", method = RequestMethod.GET)
    public ResponseEntity<Integer> con(){
        log.info("test start:{}",System.currentTimeMillis());
        roleMapper.selectRoleAll();
        log.info("test end:{}",System.currentTimeMillis());
        return ResponseEntity.ok(0);
    }

    public static byte[] toByteArray(File file) throws IOException {
        File f = file;
        if (!f.exists()) {
            throw new FileNotFoundException("file not exists");
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(f));
            int buf_size = 1024;
            byte[] buffer = new byte[buf_size];
            int len = 0;
            while (-1 != (len = in.read(buffer, 0, buf_size))) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            bos.close();
        }
    }
}
