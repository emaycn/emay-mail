package cn.emay.mail.common;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * 邮件实体
 * 
 * @author Frank
 *
 */
public class MailBody {

	/**
	 * 邮件ID
	 */
	private String messageId;

	/**
	 * 发件人
	 */
	private Linkman from;

	/**
	 * 收件人
	 */
	private List<Linkman> to;

	/**
	 * 抄送人
	 */
	private List<Linkman> cc;

	/**
	 * 密送人
	 */
	private List<Linkman> bcc;

	/**
	 * 主题
	 */
	private String subject;

	/**
	 * 发送时间
	 */
	private Date sentTime;

	/**
	 * 是否需要回执
	 */
	private boolean isNeedReply;

	/**
	 * 是否有附件
	 */
	private boolean isHasAttach;

	/**
	 * 内容
	 */
	private String content;

	/**
	 * 附件
	 */
	private File[] attachs;

	public MailBody() {

	}

	/**
	 * 发送使用
	 * 
	 * @param to
	 *            发件人
	 * @param cc
	 *            抄送人
	 * @param bcc
	 *            密送人
	 * @param subject
	 *            主题
	 * @param attachs
	 *            附件
	 * @param content
	 *            内容
	 */
	public MailBody(Linkman from, List<Linkman> to, List<Linkman> cc, List<Linkman> bcc, String subject, File[] attachs, String content) {
		this.from = from;
		this.to = to;
		this.cc = cc;
		this.bcc = bcc;
		this.subject = subject;
		this.attachs = attachs;
		this.content = content;
	}

	public Linkman getFrom() {
		return from;
	}

	public void setFrom(Linkman from) {
		this.from = from;
	}

	public List<Linkman> getTo() {
		return to;
	}

	public void setTo(List<Linkman> to) {
		this.to = to;
	}

	public List<Linkman> getCc() {
		return cc;
	}

	public void setCc(List<Linkman> cc) {
		this.cc = cc;
	}

	public List<Linkman> getBcc() {
		return bcc;
	}

	public void setBcc(List<Linkman> bcc) {
		this.bcc = bcc;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Date getSentTime() {
		return sentTime;
	}

	public void setSentTime(Date sentTime) {
		this.sentTime = sentTime;
	}

	public File[] getAttachs() {
		return attachs;
	}

	public void setAttachs(File[] attachs) {
		this.attachs = attachs;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isNeedReply() {
		return isNeedReply;
	}

	public void setNeedReply(boolean isNeedReply) {
		this.isNeedReply = isNeedReply;
	}

	public boolean isHasAttach() {
		return isHasAttach;
	}

	public void setHasAttach(boolean isHasAttach) {
		this.isHasAttach = isHasAttach;
	}

}
