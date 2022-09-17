package club.dbg.cms.admin.controller;

import club.dbg.cms.admin.filter.pojo.MyHttpServletRequest;
import club.dbg.cms.admin.service.esp.EspService;
import club.dbg.cms.admin.service.esp.pojo.EspLoginDTO;
import club.dbg.cms.admin.service.esp.pojo.EspWeatherDataDTO;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/esp")
public class EspController {
    private final EspService espService;

    public EspController(EspService espService) {
        this.espService = espService;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, name = "esp登录")
    public String login(
            @ApiIgnore MyHttpServletRequest request,
            @RequestBody EspLoginDTO loginDTO) {
        return espService.login(request.getOperator(), loginDTO);
    }

    @RequestMapping(value = "/receive", method = RequestMethod.POST, name = "esp数据接收")
    public String receive(
            MyHttpServletRequest request,
            @RequestBody EspWeatherDataDTO loginDTO
    ) {
        return espService.receive(request.getOperator(), loginDTO);
    }
}
