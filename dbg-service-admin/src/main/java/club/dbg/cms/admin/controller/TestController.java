package club.dbg.cms.admin.controller;

import club.dbg.cms.admin.dao.RoleMapper;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;

/**
 * @author dbg
 */
@RestController
public class TestController {
    private final static Logger log = LoggerFactory.getLogger(TestController.class);

    private Integer count = 0;

    private final RoleMapper roleMapper;

    public TestController(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    @RequestMapping(value = "test", method = RequestMethod.POST)
    public ResponseEntity<String> test(@RequestParam("set")HashSet<Integer> set) {
        HashSet<Integer> setA = new HashSet<>();
        setA.add(2);
        setA.add(3);
        setA.add(4);
        set.addAll(setA);
        return ResponseEntity.ok(JSON.toJSONString(set));
    }

    @RequestMapping(value = "jmeter", method = RequestMethod.GET)
    public ResponseEntity<Integer> jmeter(){
        count = count + 1;
        return ResponseEntity.ok(count);
    }

    @RequestMapping(value = "con", method = RequestMethod.GET)
    public ResponseEntity<Integer> con(){
        log.info("test start:{}",System.currentTimeMillis());
        roleMapper.selectRoleAll();
        log.info("test end:{}",System.currentTimeMillis());
        return ResponseEntity.ok(0);
    }
}
