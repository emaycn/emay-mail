package cn.emay.mail.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import cn.emay.mail.common.Linkman;
import cn.emay.mail.common.MailBody;
import cn.emay.mail.receiver.MailReceiveFilter;

/**
 * 邮件解析器
 * 
 * @author Frank
 *
 */
public class MailParser {

	/**
	 * 单例
	 */
	private static MailParser parser = new MailParser();

	/**
	 * 单例
	 */
	private MailParser() {

	}

	/**
	 * 单例
	 */
	public static MailParser getInstance() {
		return parser;
	}

	/**
	 * 解析
	 * 
	 * @param message
	 *            邮件
	 * @param attachFolderPath
	 *            附件保存地址
	 * @param filter
	 *            拦截器
	 * @return
	 * @throws MessagingException
	 * @throws IOException
	 */
	public MailBody parse(Message message, MailReceiveFilter filter, String attachFolderPath) throws MessagingException, IOException {
		if (message == null) {
			return null;
		}
		MimeMessage mimsg = (MimeMessage) message;
		Linkman from = getFrom(mimsg);
		List<Linkman> to = getRecipients(mimsg, RecipientType.TO);
		List<Linkman> cc = getRecipients(mimsg, RecipientType.CC);
		List<Linkman> bcc = getRecipients(mimsg, RecipientType.BCC);
		String subject = getSubject(mimsg);
		Date sentTime = getSentDate(mimsg);
		boolean isNeedReply = getReplySign(mimsg);
		boolean isHasAttach = isContainAttach(message);
		String messageId = mimsg.getMessageID();
		String content = getMailContent(message);
		MailBody mail = new MailBody(from, to, cc, bcc, subject, null, content);
		mail.setSentTime(sentTime);
		mail.setNeedReply(isNeedReply);
		mail.setHasAttach(isHasAttach);
		mail.setMessageId(messageId);
		if (filter != null && !filter.filter(mail)) {
			return null;
		}
		File folder = null;
		if (isHasAttach) {
			String messageIdnew = messageId == null ? UUID.randomUUID().toString().replace("-", "") : messageId.replace("<", "").replace(">", "").replace("@", "").replace(".", "").replace("$", "");
			String folderPath = attachFolderPath + File.separator + new SimpleDateFormat("yyyyMMdd").format(new Date()) + File.separator + messageIdnew;
			folder = new File(folderPath);
			if (!folder.exists()) {
				folder.mkdirs();
			}
			saveAttachMent(folderPath, message);
		}
		File[] files = isHasAttach ? folder.listFiles() : null;
		mail.setAttachs(files);
		return mail;
	}

	/**
	 * 保存附件
	 * 
	 * @throws IOException
	 * @throws MessagingException
	 */
	public void saveAttachMent(String folderPath, Part part) throws MessagingException, IOException {
		String fileName = "";
		if (part.isMimeType("multipart/*")) {
			Multipart mp = (Multipart) part.getContent();
			int counts = mp.getCount();
			for (int i = 0; i < counts; i++) {
				BodyPart mpart = mp.getBodyPart(i);
				String disposition = mpart.getDisposition();
				if ((disposition != null) && ((disposition.equals(Part.ATTACHMENT)) || (disposition.equals(Part.INLINE)))) {
					fileName = mpart.getFileName();
					fileName = MimeUtility.decodeText(fileName);
					saveFile(folderPath, fileName, mpart.getInputStream());
				} else if (mpart.isMimeType("multipart/*")) {
					saveAttachMent(folderPath, mpart);
				} else {
					fileName = mpart.getFileName();
					if ((fileName != null)) {
						fileName = MimeUtility.decodeText(fileName);
						saveFile(folderPath, fileName, mpart.getInputStream());
					}
				}
			}
		} else if (part.isMimeType("message/rfc822")) {
			saveAttachMent(folderPath, (Part) part.getContent());
		}
	}

	/**
	 * 判断此邮件是否包含附件
	 * 
	 * @throws MessagingException
	 * @throws IOException
	 */
	private boolean isContainAttach(Part part) throws IOException, MessagingException {
		boolean attachflag = false;
		if (part.isMimeType("multipart/*")) {
			Multipart mp = (Multipart) part.getContent();
			int counts = mp.getCount();
			for (int i = 0; i < counts; i++) {
				BodyPart mpart = mp.getBodyPart(i);
				String disposition = mpart.getDisposition();
				if ((disposition != null) && ((disposition.equals(Part.ATTACHMENT)) || (disposition.equals(Part.INLINE))))
					attachflag = true;
				else if (mpart.isMimeType("multipart/*")) {
					attachflag = isContainAttach((Part) mpart);
				} else {
					String contype = mpart.getContentType();
					if (contype.toLowerCase().indexOf("application") != -1)
						attachflag = true;
					if (contype.toLowerCase().indexOf("name") != -1)
						attachflag = true;
				}
			}
		} else if (part.isMimeType("message/rfc822")) {
			attachflag = isContainAttach((Part) part.getContent());
		}
		return attachflag;
	}

	/**
	 * 解析邮件，把得到的邮件内容保存到一个StringBuffer对象中，解析邮件 主要是根据MimeType类型的不同执行不同的操作，一步一步的解析
	 * 
	 * @throws IOException,MessagingException
	 */
	private String getMailContent(Part part) throws MessagingException, IOException {
		StringBuffer bodytext = new StringBuffer();
		boolean conname = false;
		if (part.getContentType().indexOf("name") != -1) {
			conname = true;
		}
		if (part.isMimeType("text/plain") && !conname) {
			bodytext.append((String) part.getContent());
		} else if (part.isMimeType("text/html") && !conname) {
			bodytext.append((String) part.getContent());
		} else if (part.isMimeType("multipart/*")) {
			Multipart multipart = (Multipart) part.getContent();
			int counts = multipart.getCount();
			for (int i = 0; i < counts; i++) {
				String st = getMailContent(multipart.getBodyPart(i));
				if (st != null && st.length() != 0) {
					bodytext.append(st);
				}
			}
		} else if (part.isMimeType("message/rfc822")) {
			String st = getMailContent((Part) part.getContent());
			if (st != null) {
				bodytext.append(st);
			}
		} else {
		}
		return bodytext.toString();
	}

	/**
	 * 是否已读[不可用]
	 * 
	 * @throws MessagingException
	 */
	protected boolean isNew(Message message) throws MessagingException {
		Flags flags = message.getFlags();
		if (flags == null) {
			return false;
		}
		for (Flags.Flag flag : flags.getSystemFlags()) {
			if (Flags.Flag.SEEN.equals(flag)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否需要回执
	 * 
	 * @throws MessagingException
	 */
	private boolean getReplySign(MimeMessage mimsg) throws MessagingException {
		return mimsg.getHeader("Disposition-Notification-To") != null;
	}

	/**
	 * 获得邮件发送日期
	 * 
	 * @throws MessagingException
	 */
	private Date getSentDate(MimeMessage mimsg) throws MessagingException {
		return mimsg.getSentDate();
	}

	/**
	 * 获得邮件主题
	 * 
	 * @throws MessagingException
	 * @throws IOException
	 */
	private String getSubject(MimeMessage mimsg) throws MessagingException, IOException {
		String subject = mimsg.getSubject();
		if (subject != null) {
			subject = MimeUtility.decodeText(subject);
		}
		return subject;
	}

	/**
	 * 获得收件人的地址和姓名
	 * 
	 * @throws MessagingException
	 */
	private List<Linkman> getRecipients(MimeMessage mimsg, RecipientType type) throws MessagingException {
		InternetAddress[] addresses = (InternetAddress[]) mimsg.getRecipients(type);
		if (addresses == null || addresses.length == 0) {
			return null;
		}
		List<Linkman> list = new ArrayList<>();
		for (InternetAddress address : addresses) {
			list.add(new Linkman(address.getAddress(), address.getPersonal()));
		}
		return list;
	}

	/**
	 * 获得发件人的地址和姓名
	 * 
	 * @throws MessagingException
	 */
	private Linkman getFrom(MimeMessage mimsg) throws MessagingException {
		InternetAddress[] address = (InternetAddress[]) mimsg.getFrom();
		if (address == null || address.length == 0) {
			return null;
		}
		String fromStr = address[0].getAddress();
		String personal = address[0].getPersonal();
		return new Linkman(fromStr, personal);
	}

	/**
	 * 【真正的保存附件到指定目录里】
	 */
	public static void saveFile(String storedir, String fileName, InputStream in) {
		File file = new File(storedir + File.separator + fileName);
		if (file.exists()) {
			file.delete();
		}
		BufferedOutputStream bos = null;
		BufferedInputStream bis = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(file));
			bis = new BufferedInputStream(in);
			byte[] bytes = new byte[1024 * 1024];
			int length;
			while ((length = bis.read(bytes)) != -1) {
				bos.write(bytes, 0, length);
			}
			bos.flush();
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					throw new IllegalArgumentException(e);
				}
			}
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					throw new IllegalArgumentException(e);
				}
			}
		}
	}

}
