package cn.emay.mail;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;

/**
 * 基于POP3的邮件接收器<br/>
 * 需要发送者邮箱先授权开启POP3协议允许
 * 
 * @author Frank
 *
 */
public class MailRecipient {

	/**
	 * pop3服务器地址
	 */
	private String pop3Host;

	/**
	 * 邮箱用户名
	 */
	private String username;

	/**
	 * 邮箱密码
	 */
	private String password;

	/**
	 * 链接
	 */
	private Session session;

	/**
	 * 
	 * @param pop3Host
	 *            pop3服务器地址
	 * @param username
	 *            邮箱用户名
	 * @param password
	 *            邮箱密码
	 */
	public MailRecipient(String pop3Host, String username, String password) {
		if (pop3Host == null) {
			throw new IllegalArgumentException("pop3Host is null");
		}
		if (username == null) {
			throw new IllegalArgumentException("username is null");
		}
		if (password == null) {
			throw new IllegalArgumentException("password is null");
		}
		Properties props = new Properties();
		props.setProperty("mail.store.protocol", "pop3");
		props.setProperty("mail.pop3.host", pop3Host);
		this.session = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		this.pop3Host = pop3Host;
		this.username = username;
		this.password = password;
	}

	/**
	 * 接收邮件
	 * 
	 * @param handler
	 *            邮件处理器
	 */
	public void receive(ReceiveHandler handler) {
		Store store = null;
		Folder folder = null;
		try {
			store = session.getStore("pop3");
			store.connect(pop3Host, username, password);
			folder = store.getFolder(handler.getFolderName());
			folder.open(Folder.READ_ONLY);
			handler.handler(folder);
		} catch (MessagingException e) {
			throw new IllegalArgumentException(e);
		} finally {
			if (folder != null) {
				try {
					folder.close();
				} catch (MessagingException e) {
					throw new IllegalArgumentException(e);
				}
			}
			if (store != null) {
				try {
					store.close();
				} catch (MessagingException e) {
					throw new IllegalArgumentException(e);
				}
			}
		}
	}

}
