package cn.emay.mail.receiver.impl;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;

import cn.emay.mail.receiver.BaseMailReceiver;

/**
 * 基于POP3的邮件接收器<br/>
 * 需要发送者邮箱先授权开启POP3协议允许
 * 
 * @author Frank
 *
 */
public class Pop3MailReceiver extends BaseMailReceiver {

	/**
	 * pop3服务器地址
	 */
	private String pop3Host;

	/**
	 * pop3服务器端口
	 */
	private int pop3Port = -1;

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
	 * 附件存储地址
	 */
	private String tmpFolderPath;

	/**
	 * 
	 * @param pop3Host
	 *            pop3服务器地址
	 * @param username
	 *            邮箱用户名
	 * @param password
	 *            邮箱密码
	 * @param tmpFolderPath
	 *            附件保存文件夹
	 */
	public Pop3MailReceiver(String pop3Host, String username, String password, String tmpFolderPath) {
		this(pop3Host, -1, username, password, tmpFolderPath);
	}

	/**
	 * 
	 * @param pop3Host
	 *            pop3服务器地址
	 * @param pop3Port
	 *            如果是默认端口，请传-1
	 * @param username
	 *            邮箱用户名
	 * @param password
	 *            邮箱密码
	 * @param tmpFolderPath
	 *            附件保存文件夹
	 */
	public Pop3MailReceiver(String pop3Host, int pop3Port, String username, String password, String tmpFolderPath) {
		if (pop3Host == null) {
			throw new IllegalArgumentException("pop3Host is null");
		}
		this.pop3Host = pop3Host;
		if (username == null) {
			throw new IllegalArgumentException("username is null");
		}
		this.username = username;
		if (password == null) {
			throw new IllegalArgumentException("password is null");
		}
		this.password = password;
		if (tmpFolderPath == null) {
			throw new IllegalArgumentException("tmpFolderPath is null");
		}
		this.tmpFolderPath = tmpFolderPath;
		Properties props = new Properties();
		props.setProperty("mail.store.protocol", "pop3");
		props.setProperty("mail.pop3.host", pop3Host);
		if (pop3Port > 0) {
			props.setProperty("mail.pop3.port", String.valueOf(pop3Port));
			this.pop3Port = pop3Port;
		}
		this.session = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
	}

	@Override
	protected Store createAndConnectStore() throws MessagingException {
		Store store = session.getStore("pop3");
		if (pop3Port < 0) {
			store.connect(pop3Host, username, password);
		} else {
			store.connect(pop3Host, pop3Port, username, password);
		}
		return store;
	}

	@Override
	protected String getAttachFolderPath() {
		return tmpFolderPath;
	}

}
