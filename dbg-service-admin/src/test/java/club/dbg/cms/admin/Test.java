package club.dbg.cms.admin;

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

    @org.junit.Test
    public void testStr() throws InterruptedException {
        String[] arr = new String[330000];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = new String(String.valueOf(System.currentTimeMillis()));
        }

        Thread.sleep(30000);
    }
}
