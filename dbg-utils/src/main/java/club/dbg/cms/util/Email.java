package club.dbg.cms.util;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class Email {

    public static boolean sendEmail(String fromEmail, String password,
                                    String email, String title,
                                    String content) {
        Properties properties = getProperties();

        try {
            // 得到回话对象
            Session session = Session.getInstance(properties);
            // 获取邮件对象
            Message message = new MimeMessage(session);
            // 设置发件人邮箱地址
            message.setFrom(new InternetAddress(fromEmail));
            // 设置收件人邮箱地址
            //message.setRecipients(Message.RecipientType.TO, new InternetAddress[]{new InternetAddress("maxiaoha@live.com")});
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));//一个收件人
            // 设置邮件标题
            message.setSubject(title);
            // 设置邮件内容
            message.setText(content);
            // 得到邮差对象
            Transport transport = session.getTransport();
            // 连接自己的邮箱账户
            transport.connect(fromEmail, password);// 密码为QQ邮箱开通的stmp服务后得到的客户端授权码
            // 发送邮件
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean sendEmail(String fromEmail, String password,
                                    String[] emails, String title,
                                    String content) {
        Properties properties = getProperties();
        try {
            // 得到回话对象
            Session session = Session.getInstance(properties);
            // 获取邮件对象
            Message message = new MimeMessage(session);
            // 设置发件人邮箱地址
            message.setFrom(new InternetAddress(fromEmail));
            // 设置收件人邮箱地址
            InternetAddress[] internetAddresses = new InternetAddress[emails.length];
            for (int i = 0; i < emails.length; i++) {
                internetAddresses[i] = new InternetAddress(emails[i]);
            }
            message.setRecipients(Message.RecipientType.TO, internetAddresses);
            // 设置邮件标题
            message.setSubject(title);
            // 设置邮件内容
            message.setText(content);
            // 得到邮差对象
            Transport transport = session.getTransport();
            // 连接自己的邮箱账户
            transport.connect(fromEmail, password);// 密码为QQ邮箱开通的stmp服务后得到的客户端授权码
            // 发送邮件
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private static Properties getProperties() {
        Properties properties = new Properties();
        properties.put("mail.transport.protocol", "smtp");// 连接协议
        properties.put("mail.smtp.host", "smtp.qq.com");// 主机名
        properties.put("mail.smtp.port", 465);// 端口号
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.enable", "true");// 设置是否使用ssl安全连接 ---一般都使用
        properties.put("mail.debug", "true");// 设置是否显示debug信息 true 会在控制台显示相关信息
        return properties;
    }
}
