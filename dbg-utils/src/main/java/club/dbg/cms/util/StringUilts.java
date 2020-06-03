package club.dbg.cms.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUilts {
    /**
     * 获取字符串中的图片链接
     * @param htmlStr html字符串
     * @return List<String>
     */
    public static List<String> getImgStr(String htmlStr) {
        List<String> list = new ArrayList<>();
        String img = "";
        Pattern pImage;
        Matcher mImage;
        // String regEx_img = "<img.*src=(.*?)[^>]*?>"; //图片链接地址
        String regExImg = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        pImage = Pattern.compile(regExImg, Pattern.CASE_INSENSITIVE);
        mImage = pImage.matcher(htmlStr);
        while (mImage.find()) {
            // 得到<img />数据
            img = mImage.group();
            // 匹配<img>中的src数据
            Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
            while (m.find()) {
                list.add(m.group(1));
            }
        }
        return list;
    }
}
