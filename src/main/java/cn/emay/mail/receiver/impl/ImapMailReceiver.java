package cn.emay.mail.receiver.impl;

import cn.emay.mail.receiver.BaseMailReceiver;

import javax.mail.*;
import java.util.Properties;

/**
 * 基于Imap的邮件接收器<br/>
 * 需要发送者邮箱先授权开启IMAP协议允许
 *
 * @author Frank
 */
public class ImapMailReceiver extends BaseMailReceiver {

    /**
     * imap服务器地址
     */
    private final String imapHost;

    /**
     * imap服务器端口
     */
    private int imapPort = -1;

    /**
     * 邮箱用户名
     */
    private final String username;

    /**
     * 邮箱密码
     */
    private final String password;

    /**
     * 链接
     */
    private final Session session;

    /**
     * 附件存储地址
     */
    private final String tmpFolderPath;

    /**
     * @param imapHost      imap服务器地址
     * @param username      邮箱用户名
     * @param password      邮箱密码
     * @param tmpFolderPath 附件保存文件夹
     */
    public ImapMailReceiver(String imapHost, String username, String password, String tmpFolderPath) {
        this(imapHost, -1, username, password, tmpFolderPath);
    }

    /**
     * @param imapHost      imap服务器地址
     * @param imapPort      如果是默认端口，请传-1
     * @param username      邮箱用户名
     * @param password      邮箱密码
     * @param tmpFolderPath 附件保存文件夹
     */
    public ImapMailReceiver(String imapHost, int imapPort, String username, String password, String tmpFolderPath) {
        if (imapHost == null) {
            throw new IllegalArgumentException("imapHost is null");
        }
        this.imapHost = imapHost;
        if (username == null) {
            throw new IllegalArgumentException("username is null");
        }
        this.username = username;
        if (password == null) {
            throw new IllegalArgumentException("password is null");
        }
        this.password = password;
        if (tmpFolderPath == null) {
            throw new IllegalArgumentException("tmpFolderPath is null");
        }
        this.tmpFolderPath = tmpFolderPath;
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imap");
        props.setProperty("mail.imap.host", imapHost);
        if (imapPort > 0) {
            props.setProperty("mail.imap.port", String.valueOf(imapPort));
            this.imapPort = imapPort;
        }
        this.session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    @Override
    protected Store createAndConnectStore() throws MessagingException {
        Store store = session.getStore("imap");
        if (imapPort < 0) {
            store.connect(imapHost, username, password);
        } else {
            store.connect(imapHost, imapPort, username, password);
        }
        return store;
    }

    @Override
    protected String getAttachFolderPath() {
        return tmpFolderPath;
    }

}
