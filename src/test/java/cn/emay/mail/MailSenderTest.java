package cn.emay.mail;

import cn.emay.mail.common.Linkman;
import cn.emay.mail.common.MailBody;
import cn.emay.mail.send.MailSender;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Frank
 */
public class MailSenderTest {

    public static void main(String[] args) {

        String stmpHost = "mail.emay.cn";
        String username = "xxxxx";
        /*使用SSL需要QQ开启 IMAP与SMTP服务 密码为开启SMTP服务时的授权码*/
        String password = "xxxxxx";

        MailSender ms = MailSender.normalSender(stmpHost, username, password);

        String subject = "你好";
        String content = "邮件内容";
        List<Linkman> to = new ArrayList<>();
        to.add(new Linkman("xxxxxx@yeah.net"));
//        File[] attachs = new File[]{new File("C:\\Users\\Frank\\Desktop\\sms-php5.php")};
        Linkman from = new Linkman("xxxxxx@emay.cn");

        MailBody mail = new MailBody(from, to, null, null, subject, null, content);

        ms.send(mail);

    }

}
