package cn.emay.mail.receiver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;

import cn.emay.mail.common.Mail;
import cn.emay.mail.util.MailParser;

/**
 * 基于POP3的邮件接收器<br/>
 * 需要发送者邮箱先授权开启POP3协议允许
 * 
 * @author Frank
 *
 */
public abstract class MailReceiver {

	protected abstract Store createAndConnectStore() throws MessagingException;

	protected abstract String getAttachFolderPath();

	/**
	 * 接收邮件
	 * 
	 * @param folderName
	 *            邮件箱名字；收件箱为inbox
	 * @param handler
	 *            邮件处理器
	 */
	public void handleMail(String folderName, MailReceiveHandler handler) {
		Store store = null;
		Folder folder = null;
		try {
			store = createAndConnectStore();
			folder = store.getFolder(folderName);
			folder.open(Folder.READ_WRITE);
			if (folder != null) {
				handler.handler(folder);
			}
		} catch (MessagingException e) {
			throw new IllegalArgumentException(e);
		} catch (IOException e) {
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

	public List<Mail> receiveInbox() {
		return receiveMails("inbox");
	}

	public List<Mail> receiveInbox(int start, int end) {
		return receiveMails("inbox", start, end);
	}

	public List<Mail> receiveMails(String folderName) {
		return receiveMails(folderName, -1, -1);
	}

	public List<Mail> receiveMails(String folderName, int start, int end) {
		List<Mail> mails = new ArrayList<Mail>();
		handleMail(folderName, new MailReceiveHandler() {
			@Override
			public void handler(Folder folder) throws MessagingException, IOException {
				int total = folder.getMessageCount();
				Message[] messages = null;
				int startnew = start <= 0 ? 1 : start;
				int endnew = end <= 0 ? total : end;
				messages = folder.getMessages(startnew, endnew);
				for (Message message : messages) {
					Mail mail = MailParser.getInstanec().parse(message,getAttachFolderPath());
					if (mail != null) {
						mails.add(mail);
					}
				}
			}
		});
		return mails;
	}

}
