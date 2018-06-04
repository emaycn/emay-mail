package cn.emay.mail;

import java.io.File;
import java.io.IOException;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;

public class TestMail {

	public static void main(String[] args) {

		String stmpHost = "mail.emay.cn";
		String pop3Host = "mail.emay.cn";
		String sender = "213123@emay.cn";
		String username = "123123";
		String password = "123123123123";

		MailSender ms = new MailSender(stmpHost, sender, username, password);

		ms.send(new String[] { "frank123@yeah.net" }, "百度来信", "你好！<a href=\"http://www.baidu.com\">点击</a>",
				new File[] { new File("C:\\Users\\Frank\\Desktop\\emay\\emay-util\\target\\classes\\cn\\emay\\util\\BigDecimalUtils.class") });

		MailRecipient rec = new MailRecipient(pop3Host, username, password);
		rec.receive(new InboxReceiveHandler() {

			@Override
			public void handler(Folder folder) throws MessagingException {
				Message[] messages = folder.getMessages();
				for (int i = 0; i < messages.length; i++) {
					Message message = messages[i];
//					Date sendTime = message.getSentDate();
//					Date receivedDate = message.getReceivedDate();
					String subject = message.getSubject();
//					Address[] from = message.getFrom();
					int number = message.getMessageNumber();
					String type= message.getContentType();
					System.out.println(number + "\n" + subject + "\n" + type + "\n");
					try {
						messages[i].getContent();//需要判断类型：混合型、文本型、附件型等，然后分别进行解析
					} catch (IOException e) {
						throw new IllegalArgumentException(e);
					}
				}
			}
		});

	}

}
