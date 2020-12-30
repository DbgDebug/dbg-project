package club.dbg.cms.util;

import java.util.regex.Pattern;

public class JavaPatternUtils {
    public static final Pattern javaPublicClassName = Pattern.compile("\\s*public\\s+class\\s+(\\w+)");
}
