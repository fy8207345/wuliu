package cn.itcast.bos.utils;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

public class MailUtils {
	private static String smtp_host = "smtp.163.com";    //网易邮箱
	private static String username = "houyuling123123@163.com";  //网易邮箱账户
	private static String password = "hyl123";     //网易邮箱授权码
	private static String from = "houyuling123123@163.com";   //使用当前账户
	public static String activeUrl = "http://localhost:9003/bos_fore/customer_activeMail";    //激活地址

	//编辑发送邮箱正文
	public static void sendMail(String subject, String content, String to) {
		Properties props = new Properties();
		props.setProperty("mail.smtp.host", smtp_host);
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.smtp.auth", "true");
		Session session = Session.getInstance(props);
		Message message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(from));
			message.setRecipient(RecipientType.TO, new InternetAddress(to));
			message.setSubject(subject);
//			message.setContent("<h3>请点击地址激活:<a href=" + activeUrl
//					+ "?activecode=" + activecode + ">" + activeUrl
//					+ "</a></h3>", "text/html;charset=utf-8");
			message.setContent(content, "text/html;charset=utf-8");
			Transport transport = session.getTransport();
			transport.connect(smtp_host, username, password);
			transport.sendMessage(message, message.getAllRecipients());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("邮件发送失败...");
		}
	}

	public static void main(String[] args) {
		sendMail("测试邮件", "你好，传智播客", "houyuling123123@163.com");
	}
}
