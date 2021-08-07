package club.dbg.cms.admin;

import org.junit.Test;

import javax.ws.rs.core.Application;
import java.io.*;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public class BaseTest {

    @Test
    public void shiftOperationTest() {
        System.out.println((-10 >>> 1));
        System.out.println(Integer.MAX_VALUE);
    }

    @Test
    public void testAsync() throws InterruptedException {
        BaseTest baseTest = new BaseTest();
        baseTest.test2();
        System.out.println("123");
        Thread.sleep(6000);
    }

    public void test2() {
        System.out.println("main函数开始执行");
        ExecutorService executor = Executors.newFixedThreadPool(2);
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(new Supplier<Integer>() {
            @Override
            public Integer get() {
                System.out.println("===task start===");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("===task finish===");
                return 3;
            }
        }, executor);
        future.thenAccept(e -> System.out.println(e));
        System.out.println("main函数执行结束");
    }

    @Test
    public void test() throws IOException, InterruptedException {
        String command = "ping www.baidu.com";
        Process process = Runtime.getRuntime().exec(command);
        InputStream is = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        process.waitFor();
        if (process.exitValue() != 0) {
            return;
        }
        String s = null;
        while ((s = reader.readLine()) != null) {
            System.out.println(s);
        }
    }

    /**
     * 获取项目加载类的根路径
     * @return
     */
    public static String getPath(){
        String path = "";
        try{
            //jar 中没有目录的概念
            URL location = Application.class.getProtectionDomain().getCodeSource().getLocation();//获得当前的URL
            File file = new File(location.getPath());//构建指向当前URL的文件描述符
            if(file.isDirectory()){//如果是目录,指向的是包所在路径，而不是文件所在路径
                path = file.getAbsolutePath();//直接返回绝对路径
            }else{//如果是文件,这个文件指定的是jar所在的路径(注意如果是作为依赖包，这个路径是jvm启动加载的jar文件名)
                path = file.getParent();//返回jar所在的父路径
            }
            System.out.println("project path=" + path);
        }catch (Exception e){
            e.printStackTrace();
        }
        return path;
    }
}
