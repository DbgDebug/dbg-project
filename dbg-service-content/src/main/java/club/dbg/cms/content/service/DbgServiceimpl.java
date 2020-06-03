package club.dbg.cms.content.service;

import club.dbg.cms.rpc.DbgService;
import org.apache.dubbo.config.annotation.Service;

@Service
public class DbgServiceimpl implements DbgService {
    @Override
    public String hello() {
        return "dubbo test";
    }
}
