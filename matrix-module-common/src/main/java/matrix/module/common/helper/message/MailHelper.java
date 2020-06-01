package matrix.module.common.helper.message;

import com.sun.net.ssl.internal.ssl.Provider;
import matrix.module.common.exception.ServiceException;
import matrix.module.common.helper.Assert;
import matrix.module.common.utils.DateUtil;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.URLDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.net.URL;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author wangcheng
 */
public class MailHelper {

    private String username;

    private String password;

    private String server;

    private boolean isSsl = false;

    static {
        Security.addProvider(new Provider());
    }

    private Session session;

    private Transport transport = null;

    public MailHelper(String username, String password, String server, Boolean isSsl) {
        Assert.isNotNull(username, "username");
        Assert.isNotNull(password, "password");
        Assert.isNotNull(server, "server");
        this.username = username;
        this.password = password;
        this.server = server;
        if (isSsl != null) {
            this.isSsl = isSsl;
        }
        //端口
        String port = server.split(":").length > 1 ? server.split(":")[1] : null;
        Properties props = System.getProperties();
        props.put("mail.smtp.auth", "true");
        props.setProperty("mail.transport.protocol", "smtp");
        if (!isSsl) {
            //非安全连接
            props.put("mail.smtp.port", port != null ? port : "25");
            this.session = Session.getInstance(props, (Authenticator) null);
        } else {
            //安全连接
            props.setProperty("mail.host", server.split(":")[0]);
            props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.ssl.enable", "true");
            props.setProperty("mail.smtp.port", port != null ? port : "465");
            props.setProperty("mail.smtp.socketFactory.port", port != null ? port : "465");
            this.session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });
        }
        this.session.setDebug(false);
    }

    public static MailHelper getInstance(String username, String password, String server, Boolean isSsl) {
        return new MailHelper(username, password, server, isSsl);
    }

    /**
     * 单人发送
     *
     * @param receiver 参数
     * @param title 参数
     * @param content 参数
     */
    public void sendOne(String receiver, String title, String content) {
        this.sendOne(receiver, title, content, null);
    }

    /**
     * 单人发送
     *
     * @param receiver
     * @param title
     * @param content
     * @param attachments
     */
    public void sendOne(String receiver, String title, String content, List<URL> attachments) {
        Assert.isNotNull(receiver, "receiver");
        List<String> receivers = new ArrayList<>();
        receivers.add(receiver);
        this.send(receivers, title, content, attachments);
    }

    /**
     * 多人发送
     *
     * @param receivers 参数
     * @param title 参数
     * @param content 参数
     */
    public void sendMany(List<String> receivers, String title, String content) {
        this.send(receivers, title, content, null);
    }

    /**
     * 多人发送
     *
     * @param receivers 参数
     * @param title 参数
     * @param content 参数
     * @param attachments 参数
     */
    public void sendMany(List<String> receivers, String title, String content, List<URL> attachments) {
        this.send(receivers, title, content, attachments);
    }

    /**
     * 发送邮件
     */
    private void send(List<String> receivers, String title, String content, List<URL> attachments) {
        Assert.state(receivers != null && receivers.size() >= 1, "receivers长度必须大于等于1");
        try {
            Address[] address = new Address[receivers.size()];
            for (int i = 0; i < receivers.size(); ++i) {
                address[i] = new InternetAddress(receivers.get(i));
            }
            MimeMessage message = new MimeMessage(this.session);
            message.setFrom(new InternetAddress(this.username));
            message.setSentDate(DateUtil.getNowDate());
            message.addRecipients(Message.RecipientType.TO, address);
            //邮件主题
            message.setSubject(MimeUtility.encodeText(title, "gb2312", "B"));
            Multipart multipart = new MimeMultipart();
            //邮件正文
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setContent(content, "text/html;charset=UTF-8");
            multipart.addBodyPart(contentPart);
            //邮件附件
            if (attachments != null && !attachments.isEmpty()) {
                for (URL url : attachments) {
                    BodyPart attachmentBodyPart = new MimeBodyPart();
                    DataSource source = new URLDataSource(url);
                    attachmentBodyPart.setDataHandler(new DataHandler(source));
                    attachmentBodyPart.setFileName(MimeUtility.encodeWord(url.getFile()));
                    multipart.addBodyPart(attachmentBodyPart);
                }
            }
            message.setContent(multipart);
            message.saveChanges();
            if (!isSsl) {
                transport = this.session.getTransport();
                transport.connect(this.server, this.username, this.password);
                transport.sendMessage(message, address);
            } else {
                Transport.send(message, address);
            }
        } catch (Exception e) {
            throw new ServiceException(e);
        } finally {
            if (transport != null) {
                try {
                    transport.close();
                } catch (MessagingException e) {
                    throw new ServiceException(e);
                }
            }
        }
    }
}
