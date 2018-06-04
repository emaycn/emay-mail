package cn.emay.mail;

import javax.mail.Folder;
import javax.mail.MessagingException;

/**
 * 接收邮件处理器
 * 
 * @author Frank
 *
 */
public interface ReceiveHandler {

	/**
	 * 邮件箱处理<br/>
	 * 不要自行关闭
	 * 
	 * @param folder
	 */
	public void handler(Folder folder) throws MessagingException;

	/**
	 * 邮件箱名称
	 * 
	 * @return
	 */
	public String getFolderName();

}
