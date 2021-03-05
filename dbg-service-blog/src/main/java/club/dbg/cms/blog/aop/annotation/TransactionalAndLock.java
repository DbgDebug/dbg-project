package club.dbg.cms.blog.aop.annotation;

import club.dbg.cms.blog.aop.LockMode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TransactionalAndLock {
    LockMode value() default  LockMode.DEFAULT_LOCK;
    int paramIndex() default -1;
}
