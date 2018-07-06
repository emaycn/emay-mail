package cn.emay.mail;

import java.util.List;

import cn.emay.mail.common.Mail;
import cn.emay.mail.receiver.impl.Pop3MailReceiver;

public class TestRecive {
	
	public static void main(String[] args) {
		
		
		String pop3Host = "mail.emay.cn";
		String username = "xxxxxx";
		String password = "xxxxxx";

		
		Pop3MailReceiver re = new Pop3MailReceiver(pop3Host, username, password, "C:\\tmp");
		
		List<Mail> list= re.receiveInbox();
		
		for(Mail mail : list) {
			System.out.println(mail.getFrom().getAddress() + "\t" + mail.getFrom().getPersonal());
		}
		
		
	}

}
