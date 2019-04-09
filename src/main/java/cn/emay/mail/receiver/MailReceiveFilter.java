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
	 * 在往磁盘写附件之前，进行拦截<br/>
	 * 参与判断的参数：to, cc, bcc, subject,
	 * content,sentTime,isNeedReply,isHasAttach,from,messageId
	 * 
	 * @param mail 邮件实体
	 * 
	 * @return true:保留，false:丢弃
	 */
	public boolean filter(MailBody mail);

}
