package club.dbg.cms.admin.service.program;

import club.dbg.cms.admin.service.program.pojo.ProgramDTO;
import club.dbg.cms.domain.admin.ProgramDO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ProgramServiceTest {
    @Autowired
    IProgramService programService;

    @Test
    public void javaCompileRunTest() {
        ProgramDO programDO = new ProgramDO();

        programDO.setSourceCode(
                "public class Test {\n" + "    public void run() {\n" +
                        "        System.out.println(\"compile run test!!!\");\n" +
                        "    }\n" +
                        "}\n");
        JavaCompileRunTask javaCompileRunTask = JavaCompileRunTask.build(programDO);
        assert javaCompileRunTask != null;
        javaCompileRunTask.run();
    }

    @Test
    public void listTest() {
        List<ProgramDTO> programDTOS = programService.list("", "java", 10, 20);
        System.out.println(programDTOS);
    }

    @Test
    public void addTest() {
        ProgramDTO programDTO = new ProgramDTO();
        programDTO.setLanguage("java");
        programDTO.setDescribed("测试");
        programDTO.setSourceCode(
                "public class Test {\n" +
                "    public void run() {\n" +
                "        System.out.println(\"compile run test!!!\");\n" +
                "    }\n" +
                "}\n");
        try {
            programService.add(programDTO);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}