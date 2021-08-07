package club.dbg.cms.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * @author dbg
 * @date 2019/08/26
 */

public class ImageUtils {
    public static String getImageBase64(String code) {
        int width = 100;
        int height = 40;
        int fontHeight = 30;
        int red = 60;
        int green = 60;
        int blue = 60;
        int lineNumber = 5;

        String imageBase64;

        BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics gd = buffImg.getGraphics();
        gd.setColor(Color.WHITE);
        gd.fillRect(0, 0, width, height);

        Font font = new Font("Fixedsys", Font.ITALIC, fontHeight);
        gd.setFont(font);

        Random random = new Random();
        gd.setColor(new Color(red, green, blue));
        Graphics2D graphics2D = (Graphics2D) gd;
        graphics2D.setStroke(new BasicStroke(3.0f));
        for (int i = 0; i < lineNumber; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int x1 = random.nextInt(width);
            int y1 = random.nextInt(height);
            graphics2D.drawLine(x, y, x1, y1);
        }

        char[] codeChars = code.toCharArray();
        for (int i = 0; i < codeChars.length; i++) {
            gd.drawString(String.valueOf(codeChars[i]), (i + 1) * 15, 30);
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ImageIO.write(buffImg, "jpg", bos);
            byte[] imageBytes = bos.toByteArray();
            imageBase64 = BASE64.encodeToString(imageBytes);
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return "data:image/jpeg;base64," + imageBase64;
    }
}
