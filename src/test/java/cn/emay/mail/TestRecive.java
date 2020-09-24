package cn.emay.mail;

import cn.emay.mail.common.MailBody;
import cn.emay.mail.receiver.impl.Pop3MailReceiver;

import java.util.List;

/**
 * @author Frank
 */
public class TestRecive {

    public static void main(String[] args) {

        String pop3Host = "mail.emay.cn";
        String username = "xxxxx";
        String password = "xxxxx";

        Pop3MailReceiver re = new Pop3MailReceiver(pop3Host, username, password, "./attc");

        List<MailBody> list = re.receiveInbox(mail -> mail.getFrom().getAddress().contains("emay.cn"));

        for (MailBody mail : list) {
            System.out.println(mail.getSubject());
        }

    }

}
