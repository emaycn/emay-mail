package cn.emay.mail;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.emay.mail.common.Linkman;
import cn.emay.mail.common.MailBody;
import cn.emay.mail.send.MailSender;

/**
 * 
 * @author Frank
 *
 */
public class MailSenderTest {

	public static void main(String[] args) {

		String stmpHost = "smtp.qq.com";
		String username = "xxx@qq.com";
		/*使用SSL需要QQ开启 IMAP与SMTP服务 密码为开启SMTP服务时的授权码*/
		String password = "xxx";

		MailSender ms = MailSender.sslMailSender(stmpHost, username, password);

		String subject = "你好";
		String content = "邮件内容";
		List<Linkman> to = new ArrayList<>();
		to.add(new Linkman("xxx@emay.cn"));
		File[] attachs = null;
		// attachs = { new File("C:\\Users\\Frank\\Desktop\\sms-php5.php") };
		Linkman from = new Linkman("xxx@qq.com");

		MailBody mail = new MailBody(from, to, null, null, subject, attachs, content);

		ms.send(mail);

	}

}
