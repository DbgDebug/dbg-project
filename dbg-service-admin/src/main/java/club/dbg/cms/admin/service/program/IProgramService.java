package club.dbg.cms.admin.service.program;

import club.dbg.cms.admin.service.program.pojo.ProgramDTO;

import java.util.List;

public interface IProgramService {
    List<ProgramDTO> list(String described, String language, Integer page, Integer pageSize);

    String execute(Integer id);

    Boolean add(ProgramDTO program);

    Boolean edit(ProgramDTO program);

    Boolean delete(Integer id);
}
