package cn.emay.mail.receiver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;

import cn.emay.mail.common.MailBody;
import cn.emay.mail.util.MailParser;

/**
 * 基于POP3的邮件接收器<br/>
 * 需要发送者邮箱先授权开启POP3协议允许
 * 
 * @author Frank
 *
 */
public abstract class MailReceiver {

	/**
	 * 创建并连接store
	 * 
	 * @return
	 * @throws MessagingException
	 */
	protected abstract Store createAndConnectStore() throws MessagingException;

	/**
	 * 获取附件保存文件夹
	 * 
	 * @return
	 */
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
			if (folder != null) {
				folder.open(Folder.READ_WRITE);
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

	/**
	 * 读取收件箱
	 * 
	 * @return
	 */
	public List<MailBody> receiveInbox() {
		return receiveMails("inbox");
	}

	/**
	 * 读取收件箱
	 * 
	 * @param filter
	 *            拦截器
	 * @return
	 */
	public List<MailBody> receiveInbox(MailReceiveFilter filter) {
		return receiveMails("inbox", filter);
	}

	/**
	 * 读取邮件文件夹
	 * 
	 * @param folderName
	 *            文件夹
	 * @return
	 */
	public List<MailBody> receiveMails(String folderName) {
		return receiveMails(folderName, null);
	}

	/**
	 * 读取邮件
	 * 
	 * @param folderName
	 *            文件夹
	 * @param filter
	 *            拦截器
	 * @return
	 */
	public List<MailBody> receiveMails(String folderName, MailReceiveFilter filter) {
		List<MailBody> mails = new ArrayList<MailBody>();
		handleMail(folderName, new MailReceiveHandler() {
			@Override
			public void handler(Folder folder) throws MessagingException, IOException {
				Message[] messages = folder.getMessages();
				for (Message message : messages) {
					MailBody mail = MailParser.getInstance().parse(message, filter, getAttachFolderPath());
					if (mail != null) {
						mails.add(mail);
					}
				}
			}
		});
		return mails;
	}

}
