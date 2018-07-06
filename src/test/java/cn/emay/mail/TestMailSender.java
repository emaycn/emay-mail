package cn.emay.mail;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.emay.mail.common.Linkman;
import cn.emay.mail.common.MailBody;
import cn.emay.mail.send.MailSender;

public class TestMailSender {

	public static void main(String[] args) {

		String stmpHost = "mail.emay.cn";
		String username = "xxxxxx";
		String password = "xxxxxx";

		MailSender ms = new MailSender(stmpHost, username, password);

		String subject = "百度来信";
		String content = "你好！<a href=\"http://www.baidu.com\">点击</a>";
		List<Linkman> to = new ArrayList<>();
		to.add(new Linkman("xxxxxx@yeah.net"));
		File[] attachs = { new File("C:\\Users\\Frank\\Desktop\\sms-php5.php") };
		Linkman from = new Linkman("xxxx@emay.cn");

		MailBody mail = new MailBody(from, to, null, null, subject, attachs, content);

		ms.send(mail);

	}

}
