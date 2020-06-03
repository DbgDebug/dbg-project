package club.dbg.cms.util.bilibili;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DanmuPatternUtils {
    /**
     * 读取 CMD 的
     */
    public static final Pattern readCmd = Pattern.compile("\"cmd\":\"(.*?)\"");
    /**
     * 读取弹幕发送者UID
     */
    public static final Pattern readDanmuUid = Pattern.compile(",\\[(\\d+)");
    /**
     * 读取弹幕发送者昵称
     */
    public static final Pattern readDanmuUser = Pattern.compile("\\[\\d+,\"(.*?)\",\\d+");
    /**
     * 读取具体弹幕内容
     */
    public static final Pattern readDanmuInfo = Pattern.compile("],\"(.*?)\",\\[");

    public static final Pattern readDanmuSendTime = Pattern.compile("\\[\\[\\d+,\\d+,\\d+,\\d+,(\\d+)");
    /**
     * 读取礼物名称
     */
    public static final Pattern readGiftName = Pattern.compile("\"giftName\":\"(.*?)\"");
    /**
     * 读取礼物数量
     */
    public static final Pattern readGiftNum = Pattern.compile("\"num\":(\\d+)");

    public static final Pattern readGiftPrice = Pattern.compile("\"price\":(\\d+)");
    /**
     * 读取礼物Id
     */
    public static final Pattern readGiftId = Pattern.compile("\"giftId\":(\\d+)");
    /**
     * 读取发送礼物者的
     */
    public static final Pattern readGiftUser = Pattern.compile("\"uname\":\"(.*?)\"");
    /**
     * 读取发送者的uid
     */
    public static final Pattern readUserId = Pattern.compile("\"uid\":(\\d+)");

    public static final Pattern readGiftSendTime = Pattern.compile("\"timestamp\":(\\d+)");
    /**
     * 读取欢迎玩家的
     */
    public static final Pattern readWelcomeUser = Pattern.compile("\"uname\":\"(.*?)\"");
    /**
     * 编码转换
     */
    public static final Pattern unicodePattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");

    /**
     * 把十六进制Unicode编码字符串转换为中文字符串
     * Ref: https://blog.csdn.net/wccmfc123/article/details/11610393
     *
     * @param str 传入的字符串，可能包含 16 位 Unicode 码
     * @return 反转义后的字符串
     */
    public static String unicodeToString(String str) {
        // 获取内部的 U 码
        Matcher matcher = DanmuPatternUtils.unicodePattern.matcher(str);
        // 字符初始化
        char ch;
        // 开始逐个替换
        while (matcher.find()) {
            // 将扒出来的 Int 转换成 char 类型，因为 Java 默认是 UTF-8 编码，所以会自动转换成对应文字
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            // 将 Unicode 码替换成对应文字，注意后面用了一个隐式类型转换
            str = str.replace(matcher.group(1), ch + "");
        }
        return str;
    }
}
