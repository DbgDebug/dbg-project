package club.dbg.cms.admin;


import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public class Test {
    @org.junit.Test
    public void bit() {
        int a = 123;
        for (int i = 0; i < 32; i++) {
            int bit = (a >> i) & 0x1;
            System.out.print(bit);
        }
    }

    public void pwd(String pwdStr) {
        String[] pwdArr = pwdStr.split(" ");
        for (String pwd : pwdArr) {
            if (pwd.length() < 8 || pwd.length() > 120) {
                System.out.println(1);
                continue;
            }
            if (pwd.matches("^(?=.*\\d)(?=.*[a-zA-Z])(?=.*[~!@#$%^&*])[\\da-zA-Z~!@#$%^&*]{8,120}$")) {
                System.out.println(0);
            } else {
                System.out.println(2);
            }
        }
    }
}
