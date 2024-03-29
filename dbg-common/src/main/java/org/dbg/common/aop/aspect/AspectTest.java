package org.dbg.common.aop.aspect;

import org.dbg.common.aop.annotation.AnnotationTest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @Author: Lingye
 * @Date: 2018/11/11
 * @Describe:
 * 定义日志切面
 * @Lazy 注解:容器一般都会在启动的时候实例化所有单实例 bean，如果我们想要 Spring 在启动的时候延迟加载 bean，需要用到这个注解
 * value为true、false 默认为true,即延迟加载，@Lazy(false)表示对象会在初始化的时候创建
 *
 * @Modified By:
 */
@Aspect
@Component
@Lazy(false)
public class AspectTest {
    /**
     * 定义切入点：对要拦截的方法进行定义与限制，如包、类
     *
     * 任意的公共方法
     * 1、execution(public * *(..))
     * 以set开头的所有的方法
     * 2、execution（* set*（..））
     * com.lingyejun.annotation.LoggerApply这个类里的所有的方法
     * 3、execution（* com.lingyejun.annotation.LoggerApply.*（..））
     * 包下的所有的类的所有的方法
     * 4、execution（* com.lingyejun.annotation.*.*（..））com.lingyejun.annotation
     * 包及子包下所有的类的所有的方法
     * 5、execution（* com.lingyejun.annotation..*.*（..））com.lingyejun.annotation
     * 包及子包下所有的类的有三个参数，第一个参数为String类型，第二个参数为任意类型，第三个参数为Long类型的方法
     * 6、execution(* com.lingyejun.annotation..*.*(String,?,Long)) com.lingyejun.annotation
     * // 有指定注解的方法
     * 7、execution(@annotation(com.lingyejun.annotation.Lingyejun))
     */
    @Pointcut("@annotation(org.dbg.common.aop.annotation.AnnotationTest)")
    private void cutMethod() {

    }

    /**
     * 前置通知：在目标方法执行前调用
     */
    @Before("cutMethod()")
    public void begin() {
        System.out.println("前置通知：在目标方法执行前调用");
    }

    /**
     * 后置通知：在目标方法执行后调用，若目标方法出现异常，则不执行
     */
    @AfterReturning("cutMethod()")
    public void afterReturning() {
        System.out.println("后置通知：在目标方法执行后调用，若目标方法出现异常，则不执行");
    }

    /**
     * 后置/最终通知：无论目标方法是否在执行过程中出现异常都会在它之后调用
     */
    @After("cutMethod()")
    public void after() {
        System.out.println("后置/最终通知：无论目标方法是否在执行过程中出现异常都会在它之后调用");
    }

    /**
     * 异常通知：目标方法抛出异常时执行
     */
    @AfterThrowing("cutMethod()")
    public void afterThrowing() {
        System.out.println("异常通知：目标方法抛出异常时执行");
    }

    /**
     * 环绕通知：灵活自由的在目标方法中切入代码
     */
    @Around("cutMethod()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("环绕通知：灵活自由的在目标方法中切入代码");
        // 获取目标方法的名称
        String methodName = joinPoint.getSignature().getName();
        // 获取方法传入参数
        Object[] params = joinPoint.getArgs();
        AnnotationTest annotationTest = getDeclaredAnnotation(joinPoint);
        // 模拟进行验证
        System.out.println(annotationTest.name() + " auth failed");
        // 执行源方法
        Object o = joinPoint.proceed();
        if(o == null){
            System.out.println("无返回值");
        }
        return o;
    }

    /**
     * 获取方法中声明的注解
     *
     * @param joinPoint
     * @return
     * @throws NoSuchMethodException
     */
    public AnnotationTest getDeclaredAnnotation(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        // 获取方法名
        String methodName = joinPoint.getSignature().getName();
        // 反射获取目标类
        Class<?> targetClass = joinPoint.getTarget().getClass();
        // 拿到方法对应的参数类型
        Class<?>[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();
        // 根据类、方法、参数类型（重载）获取到方法的具体信息
        Method objMethod = targetClass.getMethod(methodName, parameterTypes);
        // 拿到方法定义的注解信息
        // 返回
        return objMethod.getDeclaredAnnotation(AnnotationTest.class);
    }
}
