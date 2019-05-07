# Mail组件

## API

```java
cn.emay.mail.send.MailSender //邮件发送器
cn.emay.mail.receiver.BaseMailReceiver //邮件接收器
cn.emay.mail.receiver..impl.Pop3MailReceiver //邮件接收器Pop3实现
cn.emay.mail.receiver.ImapMailReceiver //邮件接收器Imap实现
```

## Pop3MailReceiver example

```java

String pop3Host = "mail.emay.cn";
String username = "xxxxxx";
String password = "xxxxxx";

Pop3MailReceiver re = new Pop3MailReceiver(pop3Host, username, password, "C:\\tmp");

List<MailBody> list = re.receiveInbox(new MailReceiveFilter() {
	@Override
	public boolean filter(MailBody mail) {
		return mail.getFrom().getAddress().contains("emay.cn");
	}
});

for ( MailBody mail : list) {
	//  mail todo
}

```

## MailSender example

```java

String stmpHost = "mail.emay.cn";
String username = "xxxxxx";
String password = "xxxxxx";

MailSender ms = new MailSender(stmpHost, username, password);

String subject = "你好";
String content = "你好！<a href=\"http://www.emay.cn\">点击</a>";
List<Linkman> to = new ArrayList<>();
to.add(new Linkman("xxxxxx@yeah.net"));
File[] attachs = null;
// attachs = { new File("C:\\Users\\Frank\\Desktop\\sms-php5.php") };
Linkman from = new Linkman("xxxx@emay.cn");

MailBody mail = new MailBody(from, to, null, null, subject, attachs, content);

ms.send(mail);

```