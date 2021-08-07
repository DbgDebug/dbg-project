package club.dbg.cms.video.controller;

import club.dbg.cms.util.ResponseBuild;
import club.dbg.cms.video.service.user.UserService;
import club.dbg.cms.video.filter.MyHttpServletRequest;
import club.dbg.cms.video.pojo.LoginDTO;
import club.dbg.cms.video.pojo.TokenDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping(value = "/user1")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<ResponseBuild<TokenDTO>> login(@RequestBody LoginDTO loginDTO) {
        return ResponseBuild.build(userService.login(loginDTO));
    }

    @RequestMapping(value = "/getWebSocketToken", method = RequestMethod.GET)
    public ResponseEntity<ResponseBuild<TokenDTO>> getWebSocketToken(MyHttpServletRequest request) {
        return ResponseBuild.build(userService.getWebSocketToken(request.getToken()));
    }

    @RequestMapping(value = "/h264init", method = RequestMethod.GET)
    public void test(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //ServletOutputStream out = response.getOutputStream();
        //out.write(WSTestController.tmpH264Byte);
        //out.flush();
        //out.close();
    }

}
