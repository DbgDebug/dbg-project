package club.dbg.cms.blog;

import club.dbg.cms.domain.admin.PermissionDO;
import club.dbg.cms.rpc.pojo.PermissionRegisterDTO;
import club.dbg.cms.util.MD5;
import club.dbg.cms.util.PackageUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dbg
 */
@Component
public class PermissionRegister {
    private static final Logger log = LoggerFactory.getLogger(PermissionRegister.class);

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Value("${spring.application.name}")
    private String serviceName;

    @Value("${system.permissionRegister}")
    private Boolean isRegister;

    @Value("${system.permissionKey}")
    private String permissionKey;

    @Value("${system.permissionRegisterUrl}")
    private String permissionRegisterUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostConstruct
    public void init() {
        if (isRegister) {
            register();
        }
    }

    private void register() {
        List<Class<?>> classes = getClassList();
        List<PermissionDO> permissions = permissionBuild(classes);
        if (permissions.isEmpty()) {
            log.info("没有需要注册的权限");
            return;
        }
        log.info(JSON.toJSONString(permissions));
        PermissionRegisterDTO permissionRegisterDTO = new PermissionRegisterDTO();
        permissionRegisterDTO.setServiceName(serviceName);
        permissionRegisterDTO.setPermissionList(permissions);
        permissionRegisterDTO.setSign(MD5.md5(serviceName + JSON.toJSONString(permissions) + permissionKey));
        ResponseEntity<String> responseEntity;
        try {
            responseEntity = restTemplate.postForEntity(permissionRegisterUrl, permissionRegisterDTO, String.class);
        } catch (Exception e) {
            log.info("注册权限失败:", e);
            return;
        }
        if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            String body = responseEntity.getBody();
            JSONObject resultJson = JSON.parseObject(body);
            Integer code = resultJson.getInteger("code");
            String message = resultJson.getString("message");
            if (code != 20000) {
                log.info("权限注册失败:{}", message);
                return;
            }
            Boolean data = resultJson.getBoolean("data");
            if (data != null && data) {
                log.info("权限注册成功");
            } else {
                log.info("权限注册失败");
            }
        } else {
            log.info("权限注册失败，请求错误，http status:{}", responseEntity.getStatusCodeValue());
        }
    }

    private List<Class<?>> getClassList() {
        List<String> classNameList = PackageUtils.getClassName("club.dbg.cms.blog.controller", false);
        log.info(JSON.toJSONString(classNameList));
        List<Class<?>> classes = new ArrayList<>();
        try {
            for (String className : classNameList) {
                classes.add(Class.forName(className));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return classes;
    }

    private List<PermissionDO> permissionBuild(List<Class<?>> classes) {
        if (contextPath == null) {
            contextPath = "";
        } else if (contextPath.indexOf("/") != 0) {
            contextPath = "/" + contextPath;
        }
        List<PermissionDO> permissions = new ArrayList<>();
        for (Class<?> classed : classes) {
            String classPath = "";
            RequestMapping classRequestMapping = classed.getAnnotation(RequestMapping.class);
            if (classRequestMapping != null) {
                String[] classPaths = classRequestMapping.value();
                if (classPaths.length > 0) {
                    if (classPaths[0].indexOf("/") != 0) {
                        classPath = "/" + classPaths[0];
                    } else {
                        classPath = classPaths[0];
                    }
                }
            }
            Method[] methods = classed.getDeclaredMethods();
            for (Method method : methods) {
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                if (requestMapping == null) {
                    continue;
                }
                PermissionDO permission = new PermissionDO();
                permission.setPermissionId(MD5.md5(serviceName + classed.getName() + method.getName()));
                String permissionName;
                permissionName = requestMapping.name().equals("") ? method.getName() : requestMapping.name();
                permission.setPermissionName(permissionName);
                permission.setMethod(classed.getName() + ": " + method.getName());
                String[] methodPaths = requestMapping.value();
                String methodPath = "";
                if (methodPaths.length > 0) {
                    if (methodPaths[0].indexOf("/") != 0) {
                        methodPath = "/" + methodPaths[0];
                    } else {
                        methodPath = methodPaths[0];
                    }
                }
                permission.setPath(contextPath + classPath + methodPath);
                permissions.add(permission);
            }
        }

        return permissions;
    }
}
