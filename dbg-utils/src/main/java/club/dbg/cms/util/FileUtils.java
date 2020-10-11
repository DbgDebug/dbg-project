package club.dbg.cms.util;

import java.io.*;

public class FileUtils {
    public static byte[] readFileByBytes(String fileName) throws IOException {
        try (InputStream in = new BufferedInputStream(new FileInputStream(fileName));
             ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            byte[] tempBytes = new byte[in.available()];
            for (int i = 0; (i = in.read(tempBytes)) != -1; ) {
                out.write(tempBytes, 0, i);
            }
            return out.toByteArray();
        }
    }

    public static void writeFileByBytes(String fileName, byte[] bytes, boolean isAppend) throws IOException{
        if(!mkdir(fileName)){
            throw new IOException("Invalid file path");
        }
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(fileName, isAppend))) {
            out.write(bytes);
        }
    }

    private static boolean mkdir(String fileName) throws IOException {
        File file = new File(fileName);
        if(!file.exists()){
            //先得到文件的上级目录，并创建上级目录，在创建文件
            if(!file.getParentFile().mkdir()){
                return false;
            }
            //创建文件
            return file.createNewFile();
        }

        return true;
    }
}
