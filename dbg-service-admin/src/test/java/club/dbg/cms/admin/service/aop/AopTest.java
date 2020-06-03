package club.dbg.cms.admin.service.aop;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@RunWith(SpringJUnit4ClassRunner.class)
public class AopTest {

    @Transactional
    synchronized public void demo() {

    }

    @Test
    public void aopTest() {
        ITest iTest = (ITest) new DynamicProxy().bind(new TestAop());
        iTest.test("业务说明");
    }
}

interface ITest {
    void test(String msg);
}

class DynamicProxy implements InvocationHandler {

    /**
     * 需要代理的目标类
     */
    private Object target;

    /**
     * 写法固定，aop专用:绑定委托对象并返回一个代理类
     *
     * @param target
     * @return
     */
    Object bind(Object target) {
        this.target = target;
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
    }

    /**
     * target：指被代理的对象。
     * method：要调用的方法
     * [] args：方法调用时所需要的参数
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        for (Object obj : args){
            System.out.println(obj instanceof String);
        }
        // 切面之前执行
        System.out.println("切面之前执行");
        // 执行业务
        result = method.invoke(target, args);
        // 切面之后执行
        System.out.println("切面之后执行");
        return result;
    }
}

/**
 * 真实主题角色：定义真实的对象。
 *
 * @author yanbin
 */
class TestAop implements ITest {
    public void test(String msg) {
        System.out.println("AOP");
    }
}
