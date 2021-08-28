package club.dbg.cms.blog.service.test;

import org.dbg.common.aop.annotation.TransactionalAndLock;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {
    private int n;
    @TransactionalAndLock
    @Override
    public void testVoid(String name) {
        n++;
    }

    @Override
    public String test() {
        return "test";
    }

    @Override
    public String test(String name) {
        return "test:" + name;
    }

    @Override
    public int getN() {
        return n;
    }
}
