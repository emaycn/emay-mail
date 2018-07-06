package cn.emay.mail.common;

import java.io.File;
import java.util.Date;
import java.util.List;

public class Mail {

	private String messageId;

	private Linkman from;

	private List<Linkman> to;

	private List<Linkman> cc;

	private List<Linkman> bcc;

	private String subject;

	private Date sentTime;

	private boolean isNeedReply;

	private boolean isNew;

	private boolean isHasAttach;

	private String content;

	private File[] attachs;

	public Mail() {

	}

	/**
	 * 发送使用
	 * 
	 * @param to
	 * @param subject
	 * @param attachs
	 */
	public Mail(List<Linkman> to, String subject, File[] attachs) {
		this.to = to;
		this.subject = subject;
		this.attachs = attachs;
	}

	/**
	 * 发送使用
	 * 
	 * @param to
	 * @param subject
	 * @param content
	 */
	public Mail(List<Linkman> to, String subject, String content) {
		this.to = to;
		this.subject = subject;
		this.content = content;
	}

	/**
	 * 发送使用
	 * 
	 * @param to
	 * @param cc
	 * @param bcc
	 * @param subject
	 * @param attachs
	 * @param content
	 */
	public Mail(List<Linkman> to, List<Linkman> cc, List<Linkman> bcc, String subject, File[] attachs, String content) {
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

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	public boolean isHasAttach() {
		return isHasAttach;
	}

	public void setHasAttach(boolean isHasAttach) {
		this.isHasAttach = isHasAttach;
	}

}
