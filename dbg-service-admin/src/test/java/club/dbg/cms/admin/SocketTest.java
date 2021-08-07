package club.dbg.cms.admin;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class SocketTest {
    @Test
    public void socketTest() throws IOException, InterruptedException {
        ServerSocket server = new ServerSocket(8887);
        Socket socket = server.accept();
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        int endCount =0;
        int readIndex = 0;
        byte[] bytes = new byte[2048];
        int c;
        while((c = inputStream.read()) != -1) {
            if(c == 13 || c == 10){
                endCount++;
                if(endCount == 2) {
                    String temp = new String(bytes, StandardCharsets.UTF_8);
                    System.out.println(temp);
                    bytes = new byte[2048];
                    readIndex = 0;
                    continue;
                }
                if(endCount == 4) {
                    break;
                }
                continue;
            }
            bytes[readIndex] = (byte) c;
            readIndex++;
            endCount = 0;
        }
        String response = "HTTP/1.1 200 OK\r\n\r\n1";
        outputStream.write(response.getBytes());
        socket.close();
    }
}
