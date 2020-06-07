package club.dbg.cms.admin;

import club.dbg.cms.domain.admin.PermissionDO;
import club.dbg.cms.rpc.PermissionRegisterService;
import club.dbg.cms.rpc.pojo.PermissionRegisterDTO;
import club.dbg.cms.util.MD5;
import club.dbg.cms.util.PackageUtils;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dbg
 */
@Component
public class PermissionRegister {
    private static final Logger log = LoggerFactory.getLogger(PermissionRegister.class);

    private final String contextPath;

    private final String serviceName;

    private final Boolean isRegister;

    private final String permissionKey;

    private final PermissionRegisterService permissionRegisterService;

    public PermissionRegister(
            @Value("${server.servlet.context-path}")
                    String contextPath,
            @Value("${spring.application.name}")
                    String serviceName,
            @Value("${system.permissionRegister}")
                    Boolean isRegister,
            @Value("${system.permissionKey}")
                    String permissionKey,
            PermissionRegisterService permissionRegisterService) {
        this.contextPath = contextPath;
        this.serviceName = serviceName;
        this.isRegister = isRegister;
        this.permissionKey = permissionKey;
        this.permissionRegisterService = permissionRegisterService;
    }

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
        if (permissionRegisterService.permissionRegister(permissionRegisterDTO)) {
            log.info("权限注册成功");
        }
    }

    private List<Class<?>> getClassList() {
        List<String> classNameList = PackageUtils.getClassName("club.dbg.cms.admin.controller", false);
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
        String contextPath = this.contextPath;

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
                String[] classPaths = classRequestMapping.path();
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
