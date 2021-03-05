package club.dbg.cms.blog.aop;

import club.dbg.cms.blog.aop.annotation.TransactionalAndLock;
import club.dbg.cms.blog.service.lock.LockService;
import club.dbg.cms.rpc.pojo.Operator;
import club.dbg.cms.util.MD5;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.lang.reflect.Method;
import java.util.concurrent.locks.ReentrantLock;

@Aspect
@Component
@Lazy(false)
public class TransactionalAndLockAspect {
    private final static Logger log = LoggerFactory.getLogger(TransactionalAndLockAspect.class);

    private final DataSourceTransactionManager dataSourceTransactionManager;

    private final LockService lockService;

    public TransactionalAndLockAspect(
            DataSourceTransactionManager dataSourceTransactionManager,
            LockService lockService) {
        this.dataSourceTransactionManager = dataSourceTransactionManager;
        this.lockService = lockService;
    }

    // 切入点，带有@TransactionalAndLock注解的方法
    @Pointcut("@annotation(club.dbg.cms.blog.aop.annotation.TransactionalAndLock)")
    private void cutMethod() {

    }

    /**
     * 环绕通知：灵活自由的在目标方法中切入代码
     */
    @Around("cutMethod()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        TransactionalAndLock transactionalAndLock = getDeclaredAnnotation(joinPoint);
        Object o;
        TransactionStatus transactionStatus = getTransactionStatus();
        ReentrantLock reentrantLock = getReentrantLock(joinPoint, transactionalAndLock);
        reentrantLock.lock();
        try {
            // 执行源方法
            o = joinPoint.proceed();
            dataSourceTransactionManager.commit(transactionStatus);
        } catch (Exception e) {
            dataSourceTransactionManager.rollback(transactionStatus);
            throw e;
        } finally {
            reentrantLock.unlock();
        }
        return o;
    }

    /**
     * @param joinPoint            切入点
     * @param transactionalAndLock 注解
     */
    private ReentrantLock getReentrantLock(ProceedingJoinPoint joinPoint, TransactionalAndLock transactionalAndLock) {
        LockMode lockMode = transactionalAndLock.value();
        if (lockMode.value().equals(LockMode.DEFAULT_LOCK.value())) {
            return defaultLock(joinPoint);
        }
        if (lockMode.value().equals(LockMode.ACCOUNT_LOCK.value())) {
            return accountLock(joinPoint, transactionalAndLock);
        }
        if (lockMode.value().equals(LockMode.ORDER_LOCK.value())) {
            return orderLock(joinPoint, transactionalAndLock);
        }
        return defaultLock(joinPoint);
    }

    private TransactionStatus getTransactionStatus() {
        DefaultTransactionDefinition transDefinition = new DefaultTransactionDefinition();
        transDefinition.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_REQUIRES_NEW);
        return dataSourceTransactionManager.getTransaction(transDefinition);
    }

    public ReentrantLock defaultLock(ProceedingJoinPoint joinPoint) {
        return lockService.defaultLock(getMethodName(joinPoint));
    }

    public ReentrantLock accountLock(ProceedingJoinPoint joinPoint, TransactionalAndLock transactionalAndLock) {
        Object[] params = joinPoint.getArgs();
        Operator operator = null;
        int paramIndex = transactionalAndLock.paramIndex();
        if (paramIndex == -1 || paramIndex >= params.length) {
            for (Object param : params) {
                if (param instanceof Operator) {
                    operator = (Operator) param;
                    break;
                }
            }
            if (operator != null) {
                return lockService.accountLock(operator.getId());
            }
        } else {
            Object param = params[paramIndex];
            if (param instanceof Operator) {
                operator = (Operator) param;
                return lockService.accountLock(operator.getId());
            }
        }
        return lockService.defaultLock(getMethodName(joinPoint));
    }

    public ReentrantLock orderLock(ProceedingJoinPoint joinPoint, TransactionalAndLock transactionalAndLock) {
        Object[] params = joinPoint.getArgs();
        Integer lockIndex;
        int paramIndex = transactionalAndLock.paramIndex();
        Object param = params[paramIndex];
        if (param instanceof Integer) {
            lockIndex = (Integer) param;
            return lockService.orderLock(lockIndex);
        }
        return lockService.defaultLock(getMethodName(joinPoint));
    }

    public String getMethodName(ProceedingJoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Class<?> targetClass = joinPoint.getTarget().getClass();
        return targetClass.getName() + "." + methodName;
    }

    /**
     * 获取方法中声明的注解
     *
     * @param joinPoint 连接点
     */
    public TransactionalAndLock getDeclaredAnnotation(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
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
        return objMethod.getDeclaredAnnotation(TransactionalAndLock.class);
    }
}
