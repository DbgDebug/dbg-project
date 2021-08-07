package club.dbg.cms.video.controller;

import club.dbg.cms.util.ResponseBuild;
import club.dbg.cms.video.filter.MyHttpServletRequest;
import club.dbg.cms.video.pojo.LoginDTO;
import club.dbg.cms.video.pojo.TokenDTO;
import club.dbg.cms.video.service.device.DeviceService;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/device1")
public class DeviceController {
    private static final Logger log = LoggerFactory.getLogger(DeviceController.class);

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<ResponseBuild<TokenDTO>> login(@RequestBody LoginDTO loginDTO) {
        System.out.println(JSON.toJSONString(loginDTO));
        return ResponseBuild.build(deviceService.login(loginDTO));
    }

    @RequestMapping(value = "/getWebSocketToken", method = RequestMethod.GET)
    public ResponseEntity<ResponseBuild<TokenDTO>> getWebSocketToken(MyHttpServletRequest request){
        return ResponseBuild.build(deviceService.getWebSocketToken(request.getToken()));
    }
}
