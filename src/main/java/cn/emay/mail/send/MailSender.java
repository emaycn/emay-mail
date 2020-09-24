package cn.emay.mail.send;

import cn.emay.mail.common.Linkman;
import cn.emay.mail.common.MailBody;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * 基于SMTP协议的邮件发送器 <br/>
 * 需要发送者邮箱先授权开启SMTP协议允许
 *
 * @author Frank
 */
public class MailSender {

    /**
     * 链接
     */
    private final Session session;

    /**
     * ssl 邮件发送器
     *
     * @param stmpHost            stmp服务器地址
     * @param port                stmp服务器端口[ssl端口默认为465]
     * @param username            发送人邮箱用户名
     * @param password            发送人邮箱密码
     * @param checkserveridentity ssl是否检测证书
     * @return 邮件发送器
     */
    public static MailSender sslMailSender(String stmpHost, String port, String username, String password, boolean checkserveridentity) {
        return new MailSender(true, stmpHost, port, username, password, checkserveridentity);
    }

    /**
     * ssl 邮件发送器<br/>
     * 使用默认的465 ssl端口
     *
     * @param stmpHost            stmp服务器地址
     * @param username            发送人邮箱用户名
     * @param password            发送人邮箱密码
     * @param checkserveridentity ssl是否检测证书
     * @return 邮件发送器
     */
    public static MailSender sslMailSender(String stmpHost, String username, String password, boolean checkserveridentity) {
        return new MailSender(true, stmpHost, null, username, password, checkserveridentity);
    }

    /**
     * ssl 邮件发送器<br/>
     * 使用默认的465 ssl端口<br/>
     * 检测证书
     *
     * @param stmpHost stmp服务器地址
     * @param username 发送人邮箱用户名
     * @param password 发送人邮箱密码
     * @return 邮件发送器
     */
    public static MailSender sslMailSender(String stmpHost, String username, String password) {
        return new MailSender(true, stmpHost, null, username, password, true);
    }

    /**
     * 普通 邮件发送器
     *
     * @param stmpHost stmp服务器地址
     * @param port     stmp服务器端口[默认端口25]
     * @param username 发送人邮箱用户名
     * @param password 发送人邮箱密码
     * @return 邮件发送器
     */
    public static MailSender normalSender(String stmpHost, String port, String username, String password) {
        return new MailSender(false, stmpHost, port, username, password, false);
    }

    /**
     * 普通 邮件发送器<br/>
     * 使用默认的25端口<br/>
     *
     * @param stmpHost stmp服务器地址
     * @param username 发送人邮箱用户名
     * @param password 发送人邮箱密码
     * @return 邮件发送器
     */
    public static MailSender normalSender(String stmpHost, String username, String password) {
        return new MailSender(false, stmpHost, null, username, password, false);
    }

    /**
     * 客户端
     *
     * @param isSsl               是否ssl链接
     * @param stmpHost            stmp服务器地址
     * @param port                stmp服务器端口[默认端口25请填空，ssl端口默认为465]
     * @param username            发送人邮箱用户名
     * @param password            发送人邮箱密码
     * @param checkserveridentity ssl是否检测证书
     */
    public MailSender(boolean isSsl, String stmpHost, String port, String username, String password, boolean checkserveridentity) {
        if (stmpHost == null) {
            throw new IllegalArgumentException("stmpHost is null");
        }
        if (username == null) {
            throw new IllegalArgumentException("username is null");
        }
        if (password == null) {
            throw new IllegalArgumentException("password is null");
        }
        if (port == null) {
            if (isSsl) {
                port = "465";
            } else {
                port = "25";
            }
        }
        Properties props = new Properties();
        props.setProperty("mail.smtp.host", stmpHost);
        props.setProperty("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.port", port);
        if (isSsl) {
            props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.setProperty("mail.smtp.socketFactory.fallback", "false");
            props.setProperty("mail.smtp.socketFactory.port", port);
            if (!checkserveridentity) {
                props.setProperty("mail.smtp.ssl.checkserveridentity", String.valueOf(false));
                props.setProperty("mail.smtp.ssl.trust", stmpHost);
            }
        }
        this.session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    /**
     * 发送邮件
     *
     * @param mail 邮件
     */
    public void send(MailBody mail) {
        if (mail == null) {
            return;
        }
        if (mail.getTo() == null || mail.getTo().isEmpty()) {
            throw new IllegalArgumentException("to is null");
        }
        if (mail.getSubject() == null) {
            throw new IllegalArgumentException("subject is null");
        }
        boolean hsNotContent = mail.getContent() == null && (mail.getAttachs() == null || mail.getAttachs().length == 0);
        if (hsNotContent) {
            throw new IllegalArgumentException("content and attachs must not be both null");
        }
        if (mail.getFrom() == null || mail.getFrom().getAddress() == null) {
            throw new IllegalArgumentException("from is null");
        }
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mail.getFrom().getAddress()));
            {
                Address[] addresses = new Address[mail.getTo().size()];
                for (int i = 0; i < mail.getTo().size(); i++) {
                    Linkman to = mail.getTo().get(i);
                    addresses[i] = new InternetAddress(to.getAddress());
                }
                message.setRecipients(MimeMessage.RecipientType.TO, addresses);
            }
            {
                if (mail.getCc() != null && mail.getCc().size() != 0) {
                    Address[] addresses = new Address[mail.getCc().size()];
                    for (int i = 0; i < mail.getCc().size(); i++) {
                        Linkman to = mail.getCc().get(i);
                        addresses[i] = new InternetAddress(to.getAddress());
                    }
                    message.setRecipients(MimeMessage.RecipientType.CC, addresses);
                }
            }
            {
                if (mail.getBcc() != null && mail.getBcc().size() != 0) {
                    Address[] addresses = new Address[mail.getBcc().size()];
                    for (int i = 0; i < mail.getBcc().size(); i++) {
                        Linkman to = mail.getBcc().get(i);
                        addresses[i] = new InternetAddress(to.getAddress());
                    }
                    message.setRecipients(MimeMessage.RecipientType.BCC, addresses);
                }
            }
            message.setSubject(mail.getSubject(), "UTF-8");
            MimeMultipart mm = new MimeMultipart();
            mm.setSubType("mixed");
            if (mail.getContent() != null) {
                MimeBodyPart contentPart = new MimeBodyPart();
                DataHandler d1 = new DataHandler(mail.getContent(), "text/html;charset=UTF-8");
                contentPart.setDataHandler(d1);
                mm.addBodyPart(contentPart);
            }
            if (mail.getAttachs() != null && mail.getAttachs().length != 0) {
                for (File file : mail.getAttachs()) {
                    MimeBodyPart attachmentPart = new MimeBodyPart();
                    DataHandler dh2 = new DataHandler(new FileDataSource(file));
                    attachmentPart.setDataHandler(dh2);
                    attachmentPart.setFileName(MimeUtility.encodeText(dh2.getName()));
                    mm.addBodyPart(attachmentPart);
                }
            }
            message.setContent(mm);
            Transport.send(message, message.getAllRecipients());
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
    }

}