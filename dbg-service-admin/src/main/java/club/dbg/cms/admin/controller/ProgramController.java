package club.dbg.cms.admin.controller;

import club.dbg.cms.admin.service.program.ProgramService;
import club.dbg.cms.admin.service.program.pojo.ProgramDTO;
import club.dbg.cms.util.ResponseBuild;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/program")
public class ProgramController {

    private final ProgramService programService;

    public ProgramController(ProgramService programService) {
        this.programService = programService;
    }

    @RequestMapping(value = "list", method = RequestMethod.GET, name = "获取程序列表")
    public ResponseBuild<List<ProgramDTO>> list(
            @RequestParam(value = "desc", required = false, defaultValue = "") String desc,
            @RequestParam("language") String language,
            @RequestParam("page") Integer page,
            @RequestParam("pageSize") Integer pageSize) {
        return ResponseBuild.ok(programService.list(desc, language, page, pageSize));
    }

    @RequestMapping(value = "add", method = RequestMethod.POST, name = "添加程序")
    public ResponseBuild<Boolean> add(@RequestBody ProgramDTO programDTO) {
        return ResponseBuild.ok(programService.add(programDTO));
    }

    @RequestMapping(value = "execute", method = RequestMethod.GET, name = "运行程序")
    public ResponseBuild<String> execute(@RequestParam("id") Integer id) {
        return ResponseBuild.ok(programService.execute(id));
    }

}
