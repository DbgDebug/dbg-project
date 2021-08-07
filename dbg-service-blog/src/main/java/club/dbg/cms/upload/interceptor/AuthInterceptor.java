package club.dbg.cms.upload.interceptor;

import club.dbg.cms.upload.config.MyWebMvcConfigurer;
import club.dbg.cms.upload.exception.BlogException;
import club.dbg.cms.upload.filter.pojo.MyHttpServletRequest;
import club.dbg.cms.upload.redis.RedisUtils;
import club.dbg.cms.rpc.pojo.Operator;
import club.dbg.cms.rpc.pojo.RoleDTO;
import club.dbg.cms.util.MD5;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;

public class AuthInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(AuthInterceptor.class);

    private final String serviceName;

    private final String roleHeader;

    private final String permissionHeader;

    private final RedisUtils redisUtils;

    private final MyWebMvcConfigurer webMvcConfigurer;

    public AuthInterceptor(String serviceName,
                           String roleHeader,
                           String permissionHeader,
                           RedisUtils redisUtils, MyWebMvcConfigurer webMvcConfigurer) {
        this.serviceName = serviceName;
        this.roleHeader = roleHeader;
        this.redisUtils = redisUtils;
        this.permissionHeader = permissionHeader;
        this.webMvcConfigurer = webMvcConfigurer;
    }

    /**
     * 验证是否有访问权限，验证错误抛出异常
     *
     * @param httpServletRequest  请求
     * @param httpServletResponse 响应
     * @param handler             控制器代理类
     * @return boolean
     * @throws Exception 异常
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse,
                             Object handler) throws Exception {
        HandlerMethod handlerMethod;
        if (handler instanceof HandlerMethod) {
            handlerMethod = (HandlerMethod) handler;
        } else {
            if(webMvcConfigurer.isDebug){
                return true;
            }
            httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
            return false;
        }

        if(webMvcConfigurer.isDebug){
            return true;
        }

        String md5Value = MD5.md5(serviceName + AopUtils.getTargetClass(handlerMethod.getBean()).getName()
                + handlerMethod.getMethod().getName());
        MyHttpServletRequest myHttpServletRequest = (MyHttpServletRequest) httpServletRequest;
        if (!permissionVerification(myHttpServletRequest.getOperator(), md5Value)) {
            throw new BlogException(9999, "Authentication Failed");
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }

    /**
     * 验证访问权限，设置操作人员信息
     * 使用权限等级最高的角色进行访问
     *
     * @param operator 操作人员
     * @param pathMD5Value     访问的类和方法的MD5值（MD5(serviceName + className + methodName)）
     */
    private Boolean permissionVerification(Operator operator, String pathMD5Value) {
        boolean result = false;
        Integer permissionId = (Integer) redisUtils.hashGet(permissionHeader, pathMD5Value);
        if (permissionId == null) {
            return false;
        }
        RoleDTO roleTemp = new RoleDTO();
        HashSet<Integer> roleIds = operator.getRoleIds();
        for (Integer roleId : roleIds) {
            RoleDTO roleDTO = (RoleDTO) redisUtils.get(roleHeader + roleId);
            if (roleDTO.getPermissionSet().contains(permissionId)) {
                if (roleTemp.getRoleLevel() == null || roleDTO.getRoleLevel() < roleTemp.getRoleLevel()) {
                    roleTemp.setId(roleDTO.getId());
                    roleTemp.setRoleLevel(roleDTO.getRoleLevel());
                    result = true;
                }
            }
        }
        return result;
    }
}