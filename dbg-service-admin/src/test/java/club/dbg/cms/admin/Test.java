package club.dbg.cms.admin;

import club.dbg.cms.util.UUIDUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.NamingException;
import java.rmi.RemoteException;

public class Test {

    @org.junit.Test
    public void str() {
        System.out.println(UUIDUtils.getUUID());
    }
    @org.junit.Test
    public void jndiTest() throws NamingException, RemoteException {

    }

    @org.junit.Test
    public void bit() {
        System.out.println(0xFF);
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

    @org.junit.Test
    public void testStr() throws InterruptedException {
        String[] arr = new String[330000];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = new String(String.valueOf(System.currentTimeMillis()));
        }

        Thread.sleep(30000);
    }
}
