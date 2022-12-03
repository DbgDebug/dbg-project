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

    public static boolean isPNG(byte[] bytes) {
        return bytes[1] == (byte) 'P'
                && bytes[2] == (byte) 'N'
                && bytes[3] == (byte) 'G';
    }

    public static boolean isJPEG(byte[] bytes) {
        if (bytes[6] == (byte) 'J'
                && bytes[7] == (byte) 'F'
                && bytes[8] == (byte) 'I'
                && bytes[9] == (byte) 'F') {
            return true;
        } else if (bytes[6] == (byte) 'E'
                && bytes[7] == (byte) 'x'
                && bytes[8] == (byte) 'i'
                && bytes[9] == (byte) 'f') {
            return true;
        } else if (bytes[6] == (byte) 'e'
                && bytes[7] == (byte) 'x'
                && bytes[8] == (byte) 'i'
                && bytes[9] == (byte) 'f') {
            return true;
        }
        return false;
    }

    public static String getImageType(byte[] bytes) {
        String type = null;
        if (bytes[1] == (byte) 'P'
                && bytes[2] == (byte) 'N'
                && bytes[3] == (byte) 'G') {
            type = ".png";
        } else if (bytes[6] == (byte) 'J'
                && bytes[7] == (byte) 'F'
                && bytes[8] == (byte) 'I'
                && bytes[9] == (byte) 'F') {
            type = ".jpg";
        } else if (bytes[6] == (byte) 'E'
                && bytes[7] == (byte) 'x'
                && bytes[8] == (byte) 'i'
                && bytes[9] == (byte) 'f') {
            type = ".jpg";
        } else if (bytes[6] == (byte) 'e'
                && bytes[7] == (byte) 'x'
                && bytes[8] == (byte) 'i'
                && bytes[9] == (byte) 'f') {
            type = ".jpg";
        } else if (bytes[0] == (byte) 0xFF
                && bytes[1] == (byte) 0xD8
                && bytes[bytes.length - 2] == (byte) 0xFF
                && bytes[bytes.length - 1] == (byte) 0xD9) {
            type = ".jpg";
        } else if (bytes[0] == (byte) 'G'
                && bytes[1] == (byte) 'I'
                && bytes[2] == (byte) 'F') {
            type = ".gif";
        }

        return type;
    }
}
