package cn.emay.mail;

/**
 * 收件箱处理器
 * 
 * @author Frank
 *
 */
public abstract class InboxReceiveHandler implements ReceiveHandler {

	@Override
	public String getFolderName() {
		return "inbox";
	}

}
