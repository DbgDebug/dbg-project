package club.dbg.cms.upload.controller;

import club.dbg.cms.rpc.DbgService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    //Reference
    private DbgService dbgService;

    @RequestMapping(value = "/esp/test", method = RequestMethod.POST)
    public void test(@RequestBody String text){
        System.out.println(text);
    }

    @RequestMapping(value = "a", method = RequestMethod.GET)
    public String a(){
        System.out.println("get a");
        return "aaaaa";
    }
}
