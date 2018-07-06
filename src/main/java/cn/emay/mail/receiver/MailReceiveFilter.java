package cn.emay.mail.receiver;

import cn.emay.mail.common.MailBody;

/**
 * 拦截器
 * 
 * @author Frank
 *
 */
public interface MailReceiveFilter {

	/**
	 * 拦截<br/>
	 * true:保留，false:丢弃<br/>
	 * 在往磁盘写附件之前，进行拦截<br/>
	 * 参与判断的参数：to, cc, bcc, subject,
	 * content,sentTime,isNeedReply,isHasAttach,from,messageId
	 * 
	 * @param folder
	 */
	public boolean filter(MailBody mail);

}
