package cn.emay.mail;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

/**
 * 基于SMTP协议的邮件发送器 <br/>
 * 需要发送者邮箱先授权开启SMTP协议允许
 * 
 * @author Frank
 *
 */
public class MailSender {

	/**
	 * 发送人邮箱
	 */
	private String senderAddress;

	/**
	 * 链接
	 */
	private Session session;

	/**
	 * 
	 * @param stmpHost
	 *            stmp服务器地址
	 * @param senderAddress
	 *            发送人邮箱
	 * @param username
	 *            发送人邮箱用户名
	 * @param password
	 *            发送人邮箱密码
	 */
	public MailSender(String stmpHost, String senderAddress, String username, String password) {
		if (stmpHost == null) {
			throw new IllegalArgumentException("stmpHost is null");
		}
		if (senderAddress == null) {
			throw new IllegalArgumentException("senderAddress is null");
		}
		if (username == null) {
			throw new IllegalArgumentException("username is null");
		}
		if (password == null) {
			throw new IllegalArgumentException("password is null");
		}
		this.senderAddress = senderAddress;
		Properties props = new Properties();
		props.setProperty("mail.smtp.auth", "true");
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.smtp.host", stmpHost);
		this.session = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
	}

	/**
	 * 发送邮件
	 * 
	 * @param toAddress
	 *            发送邮箱地址
	 * @param subject
	 *            邮件主题
	 * @param content
	 *            邮件内容
	 */
	public void send(String toAddress, String subject, String content) {
		send(new String[] { toAddress }, subject, content);
	}

	/**
	 * 发送邮件
	 * 
	 * @param toAddresses
	 *            发送邮箱地址
	 * @param subject
	 *            邮件主题
	 * @param content
	 *            邮件内容
	 */
	public void send(String[] toAddresses, String subject, String content) {
		send(toAddresses, null, subject, content);
	}

	/**
	 * 发送邮件
	 * 
	 * @param toAddresses
	 *            发送邮箱地址
	 * @param ccAddresses
	 *            抄送邮箱地址
	 * @param subject
	 *            邮件主题
	 * @param content
	 *            邮件内容
	 */
	public void send(String[] toAddresses, String[] ccAddresses, String subject, String content) {
		send(toAddresses, ccAddresses, subject, content, null);
	}
	
	/**
	 * 发送邮件
	 * 
	 * @param toAddresses
	 *            发送邮箱地址
	 * @param subject
	 *            邮件主题
	 * @param content
	 *            邮件内容
	 * @param attachmentFiles
	 *            附件
	 */
	public void send(String[] toAddresses,  String subject, String content, File[] attachmentFiles) {
		send(toAddresses, null, subject, content, null, attachmentFiles);
	}

	/**
	 * 发送邮件
	 * 
	 * @param toAddresses
	 *            发送邮箱地址
	 * @param ccAddresses
	 *            抄送邮箱地址
	 * @param subject
	 *            邮件主题
	 * @param content
	 *            邮件内容
	 * @param attachmentFiles
	 *            附件
	 */
	public void send(String[] toAddresses, String[] ccAddresses, String subject, String content, File[] attachmentFiles) {
		send(toAddresses, ccAddresses, subject, content, null, attachmentFiles);
	}

	/**
	 * 发送邮件
	 * 
	 * @param toAddresses
	 *            发送邮箱地址
	 * @param ccAddresses
	 *            抄送邮箱地址
	 * @param subject
	 *            邮件主题
	 * @param content
	 *            邮件内容
	 * @param contentType
	 *            邮件内容类型
	 * @param attachmentFiles
	 *            附件
	 * @throws IllegalArgumentException
	 */
	public void send(String[] toAddresses, String[] ccAddresses, String subject, String content, String contentType, File[] attachmentFiles) {
		if (toAddresses == null || toAddresses.length == 0) {
			throw new IllegalArgumentException("toAddresses is null");
		}
		if (subject == null) {
			throw new IllegalArgumentException("subject is null");
		}
		if (content == null && (attachmentFiles == null || attachmentFiles.length == 0)) {
			throw new IllegalArgumentException("content and attachmentFiles must not be both null");
		}
		Transport transport = null;
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(senderAddress));
			if (toAddresses != null && toAddresses.length != 0) {
				Address[] addresses = new Address[toAddresses.length];
				for (int i = 0; i < toAddresses.length; i++) {
					addresses[i] = new InternetAddress(toAddresses[i]);
				}
				message.setRecipients(MimeMessage.RecipientType.TO, addresses);
			}
			if (ccAddresses != null && ccAddresses.length != 0) {
				Address[] addresses = new Address[ccAddresses.length];
				for (int i = 0; i < ccAddresses.length; i++) {
					addresses[i] = new InternetAddress(ccAddresses[i]);
				}
				message.setRecipients(MimeMessage.RecipientType.CC, addresses);
			}
			if (subject != null) {
				message.setSubject(subject, "UTF-8");
			}
			MimeMultipart mm = new MimeMultipart();
			mm.setSubType("mixed");
			if (content != null) {
				MimeBodyPart contentPart = new MimeBodyPart();
				if (contentType == null) {
					contentType = "text/html;charset=UTF-8";
				}
				DataHandler d1 = new DataHandler(content, contentType);
				contentPart.setDataHandler(d1);
				mm.addBodyPart(contentPart);
			}
			if (attachmentFiles != null && attachmentFiles.length > 0) {
				for (File file : attachmentFiles) {
					MimeBodyPart attachmentPart = new MimeBodyPart();
					DataHandler dh2 = new DataHandler(new FileDataSource(file));
					attachmentPart.setDataHandler(dh2);
					attachmentPart.setFileName(MimeUtility.encodeText(dh2.getName()));
					mm.addBodyPart(attachmentPart);
				}
			}
			message.setContent(mm);
			transport = session.getTransport();
			Transport.send(message, message.getAllRecipients());
		} catch (MessagingException e) {
			throw new IllegalArgumentException(e);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		} finally {
			if (transport != null) {
				try {
					transport.close();
				} catch (MessagingException e) {
					throw new IllegalArgumentException(e);
				}
			}
		}
	}

}
