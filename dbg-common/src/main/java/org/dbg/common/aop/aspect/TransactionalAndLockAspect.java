package org.dbg.common.aop.aspect;

import club.dbg.cms.rpc.pojo.Operator;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.dbg.common.aop.annotation.TransactionalAndLock;
import org.dbg.common.aop.lock.LockMode;
import org.dbg.common.aop.lock.LockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.lang.reflect.Method;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 测试中
 */
@Aspect
@Lazy(false)
public class TransactionalAndLockAspect {
    private final static Logger log = LoggerFactory.getLogger(TransactionalAndLockAspect.class);

    private final DataSourceTransactionManager dataSourceTransactionManager;

    private final LockService lockService;

    public TransactionalAndLockAspect(
            DataSourceTransactionManager dataSourceTransactionManager) {
        this.dataSourceTransactionManager = dataSourceTransactionManager;
        this.lockService = LockService.getInstance();
    }

    // 切入点，带有@TransactionalAndLock注解的方法
    @Pointcut("@annotation(org.dbg.common.aop.annotation.TransactionalAndLock)")
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
     * 根据LockMode获取可重入锁
     * 当获取不到指定模式的锁的时候将获取默认锁
     *
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

    public ReentrantLock defaultLock(ProceedingJoinPoint joinPoint) {
        return lockService.defaultLock(getMethodName(joinPoint));
    }

    /**
     * 当获取不到账号锁的时候将获取默认锁
     *
     * @param joinPoint            切入点
     * @param transactionalAndLock 注解
     */
    public ReentrantLock accountLock(ProceedingJoinPoint joinPoint, TransactionalAndLock transactionalAndLock) {
        Object[] params = joinPoint.getArgs();
        Operator operator;
        int paramIndex = transactionalAndLock.paramIndex();

        if (paramIndex >= 0 && params.length > paramIndex) {
            Object param = params[paramIndex];
            if (param instanceof Operator) {
                operator = (Operator) param;
                Integer id = operator.getId();
                if (id != null) {
                    return lockService.accountLock(id);
                }
            }
        }

        for (Object obj : params) {
            if (obj instanceof Operator) {
                operator = (Operator) obj;
                Integer id = operator.getId();
                if (id != null) {
                    return lockService.accountLock(id);
                }
            }
        }

        return lockService.defaultLock(getMethodName(joinPoint));
    }

    /**
     * 当获取不到订单锁的时候将获取默认锁
     *
     * @param joinPoint            切入点
     * @param transactionalAndLock 注解
     */
    public ReentrantLock orderLock(ProceedingJoinPoint joinPoint, TransactionalAndLock transactionalAndLock) {
        Object[] params = joinPoint.getArgs();
        Integer lockIndex;
        int paramIndex = transactionalAndLock.paramIndex();
        if (paramIndex >= 0 && params.length > paramIndex) {
            Object param = params[paramIndex];
            if (param instanceof Integer) {
                lockIndex = (Integer) param;
                return lockService.orderLock(lockIndex);
            }
        }

        return lockService.defaultLock(getMethodName(joinPoint));
    }

    /**
     * 获取访问的方法名（全路径名：className + method）
     *
     * @param joinPoint 切入点
     */
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

    private TransactionStatus getTransactionStatus() {
        DefaultTransactionDefinition transDefinition = new DefaultTransactionDefinition();
        transDefinition.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_REQUIRED);
        return dataSourceTransactionManager.getTransaction(transDefinition);
    }
}
