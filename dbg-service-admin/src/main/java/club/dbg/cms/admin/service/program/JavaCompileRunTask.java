package club.dbg.cms.admin.service.program;

import club.dbg.cms.domain.admin.ProgramDO;
import club.dbg.cms.util.FileUtils;
import club.dbg.cms.util.JavaPatternUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.regex.Matcher;

public class JavaCompileRunTask extends ClassLoader implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(JavaCompileRunTask.class);

    private static final String javaFileSuffix = ".java";

    private static final String javaClassSuffix = ".class";

    private final ProgramDO program;

    private final String sourceFile;

    private final String executableFile;

    private JavaCompileRunTask(String sourceFile, String executableFile, ProgramDO program) {
        this.program = program;
        this.sourceFile = sourceFile;
        this.executableFile = executableFile;
    }

    public static JavaCompileRunTask build(ProgramDO program) {
        String className = getClassName(program.getSourceCode());
        if (className == null) {
            log.warn("class name error:{}", program.getSourceCode());
            return null;
        }
        return new JavaCompileRunTask(
                ".\\java_test\\" + className + javaFileSuffix,
                ".\\java_test\\" + className + javaClassSuffix,
                program);
    }

    @Override
    public void run() {
        try {
            Thread.sleep(5000);
            if (!compile(program)) {
                return;
            }

            execute(executableFile);
        } catch (Exception e) {
            log.error("Java compile run exception:", e);
        }
    }

    private static String getClassName(String sourceCode) {
        Matcher matcher = JavaPatternUtils.javaPublicClassName.matcher(sourceCode);
        if (!matcher.find()) {
            return null;
        }
        return matcher.group(1);
    }

    private boolean compile(ProgramDO program) throws IOException, InterruptedException {
        FileUtils.writeFileByBytes(sourceFile, program.getSourceCode().getBytes(), false);
        Process process = Runtime.getRuntime().exec("javac -encoding utf-8 " + sourceFile);
        InputStream is = process.getInputStream();
        // process.getErrorStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        process.waitFor();
        String msg;
        StringBuilder tmp = new StringBuilder();
        while ((msg = reader.readLine()) != null) {
            tmp.append(msg);
            log.info(msg);
        }
        reader.close();
        if (!tmp.toString().equals("")) {
            log.warn("Java compile failed:{}", sourceFile);
            return false;
        }
        File file = new File(executableFile);
        if (!file.exists()) {
            log.warn("Executable file not found:{}", executableFile);
            return false;
        }

        return true;
    }

    private void execute(String executableFilePath) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        byte[] executableFileBytes = FileUtils.readFileByBytes(executableFilePath);
        Class<?> c = super.defineClass(null, executableFileBytes, 0, executableFileBytes.length);
        c.getMethod("run").invoke(c.getDeclaredConstructor().newInstance());
    }

}
