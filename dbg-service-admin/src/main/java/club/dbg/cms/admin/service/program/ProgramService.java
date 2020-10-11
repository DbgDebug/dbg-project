package club.dbg.cms.admin.service.program;

import club.dbg.cms.admin.dao.ProgramMapper;
import club.dbg.cms.admin.exception.BusinessException;
import club.dbg.cms.admin.service.asynctask.AsyncTaskService;
import club.dbg.cms.admin.service.program.pojo.ProgramDTO;
import club.dbg.cms.domain.admin.ProgramDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ProgramService implements IProgramService {
    private static final Logger log = LoggerFactory.getLogger(ProgramService.class);

    private final AsyncTaskService asyncTaskService;

    private final ProgramMapper programMapper;

    public ProgramService(AsyncTaskService asyncTaskService, ProgramMapper programMapper) {
        this.asyncTaskService = asyncTaskService;
        this.programMapper = programMapper;
    }

    @Override
    public List<ProgramDTO> list(String desc, String language, Integer page, Integer pageSize) {
        page = (page - 1) * pageSize;
        List<ProgramDO> programDOS = programMapper.selectList("%" + desc + "%", language, page, pageSize);
        if(programDOS == null || programDOS.isEmpty()){
            return new ArrayList<>();
        }
        List<ProgramDTO> programDTOS = new ArrayList<>(programDOS.size());
        for (ProgramDO programDO : programDOS) {
            programDTOS.add(new ProgramDTO(programDO));
        }

        return programDTOS;
    }

    @Override
    public String execute(Integer id) {
        ProgramDO programDO = programMapper.selectById(id);
        if(programDO == null){
            throw new BusinessException("运行失败");
        }
        JavaCompileRunTask javaCompileRunTask = JavaCompileRunTask.build(programDO);
        asyncTaskService.execute(javaCompileRunTask, 10L, TimeUnit.SECONDS);
        return "success";
    }

    @Override
    public Boolean add(ProgramDTO programDTO) {
        ProgramDO programDO = new ProgramDO();
        programDO.setLanguage(programDTO.getLanguage());
        programDO.setDescribed(programDTO.getDescribed());
        programDO.setSourceCode(programDTO.getSourceCode());
        programDO.setCreateTime(System.currentTimeMillis() / 1000);
        programDO.setUpdateTime(System.currentTimeMillis() / 1000);
        programDO.setStatus(0);
        JavaCompileRunTask javaCompileRunTask = JavaCompileRunTask.build(programDO);
        if(javaCompileRunTask == null){
            throw new BusinessException("提交失败，构建运行任务失败");
        }

        if(programMapper.insert(programDO) <= 0){
            throw new BusinessException("提交失败，保存代码失败");
        }

        asyncTaskService.execute(javaCompileRunTask, 10L, TimeUnit.SECONDS);
        return true;
    }

    @Override
    public Boolean edit(ProgramDTO program) {
        ProgramDO programDO = programMapper.selectById(program.getId());
        if(programDO == null){
            throw new BusinessException("更新失败，数据不存在");
        }
        programDO.setSourceCode(program.getSourceCode());
        programDO.setUpdateTime(System.currentTimeMillis() / 1000);
        if(programMapper.update(programDO) < 1){
            throw new BusinessException("更新失败");
        }
        asyncTaskService.execute(JavaCompileRunTask.build(programDO), 3L, TimeUnit.SECONDS);
        return true;
    }

    @Override
    public Boolean delete(Integer id) {
        if(programMapper.delete(id) < 1){
            throw new BusinessException("删除失败");
        }
        return true;
    }
}
