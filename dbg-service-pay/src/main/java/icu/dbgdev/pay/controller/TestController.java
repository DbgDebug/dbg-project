package icu.dbgdev.pay.controller;

import club.dbg.cms.util.ResponseBuild;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @RequestMapping(value = "/a", method = RequestMethod.GET)
    public ResponseBuild<String> test (@RequestParam("id") Integer id) {
        return ResponseBuild.ok("测试:" + id);
    }
}
