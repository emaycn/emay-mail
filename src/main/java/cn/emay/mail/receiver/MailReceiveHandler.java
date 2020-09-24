package cn.emay.mail.receiver;

import javax.mail.Folder;
import javax.mail.MessagingException;
import java.io.IOException;

/**
 * 接收邮件处理器
 *
 * @author Frank
 */
public interface MailReceiveHandler {

    /**
     * 邮件箱处理<br/>
     * 不要自行关闭
     *
     * @param folder 邮件文件夹
     * @throws MessagingException 信息异常
     * @throws IOException        IO异常
     */
    void handler(Folder folder) throws MessagingException, IOException;

}
