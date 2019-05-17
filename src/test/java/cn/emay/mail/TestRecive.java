package cn.emay.mail;

import java.util.List;

import cn.emay.mail.common.MailBody;
import cn.emay.mail.receiver.MailReceiveFilter;
import cn.emay.mail.receiver.impl.Pop3MailReceiver;

/**
 * 
 * @author Frank
 *
 */
public class TestRecive {

	@SuppressWarnings("unused")
	public static void main(String[] args) {

		String pop3Host = "mail.emay.cn";
		String username = "xxxxxx";
		String password = "xxxxxx";

		Pop3MailReceiver re = new Pop3MailReceiver(pop3Host, username, password, "C:\\tmp");

		List<MailBody> list = re.receiveInbox(new MailReceiveFilter() {
			@Override
			public boolean filter(MailBody mail) {
				return mail.getFrom().getAddress().contains("emay.cn");
			}
		});

		for (MailBody mail : list) {
			// mail todo
		}

	}

}
