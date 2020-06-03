package club.dbg.cms.blog.controller;

import club.dbg.cms.rpc.DbgService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Reference
    private DbgService dbgService;

    @RequestMapping(value = "test", method = RequestMethod.GET)
    public String test(){
        return dbgService.hello();
    }

    @RequestMapping(value = "a", method = RequestMethod.GET)
    public String a(){
        return "aaaaa";
    }
}
