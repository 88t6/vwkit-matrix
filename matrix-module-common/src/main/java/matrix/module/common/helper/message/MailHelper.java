package matrix.module.common.helper.message;

import com.sun.net.ssl.internal.ssl.Provider;
import matrix.module.common.exception.ServiceException;
import matrix.module.common.helper.Assert;
import matrix.module.common.utils.DateUtil;
import org.apache.commons.collections4.CollectionUtils;

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
        Assert.notNullTip(username, "username");
        Assert.notNullTip(password, "password");
        Assert.notNullTip(server, "server");
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
        if (!this.isSsl) {
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
     * @param receiver 接收人
     * @param title 标题
     * @param content 内容
     */
    public void sendOne(String receiver, String title, String content) {
        this.sendOne(receiver, title, content, null);
    }

    /**
     * 单人发送
     * @param receiver 接收人
     * @param title 标题
     * @param content 内容
     * @param attachments 附件
     */
    public void sendOne(String receiver, String title, String content, List<URL> attachments) {
        Assert.notNullTip(receiver, "receiver");
        List<String> receivers = new ArrayList<>();
        receivers.add(receiver);
        this.send(receivers, null, null, title, content, attachments);
    }

    /**
     * 多人发送
     * @param receivers 接收人
     * @param title 标题
     * @param content 内容
     */
    public void sendMany(List<String> receivers, String title, String content) {
        this.sendMany(receivers, null, null, title, content, null);
    }

    /**
     * 多人发送
     * @param receivers 接收人
     * @param ccReceivers 抄送人
     * @param bccReceivers 密送人
     * @param title 标题
     * @param content 内容
     */
    public void sendMany(List<String> receivers, List<String> ccReceivers, List<String> bccReceivers, String title, String content) {
        this.send(receivers, ccReceivers, bccReceivers, title, content, null);
    }

    /**
     * 多人发送
     * @param receivers 接收人
     * @param title 标题
     * @param content 内容
     * @param attachments 附件
     */
    public void sendMany(List<String> receivers, String title, String content, List<URL> attachments) {
        this.sendMany(receivers, null, null, title, content, attachments);
    }

    /**
     * 多人发送
     * @param receivers 接收人
     * @param ccReceivers 抄送人
     * @param bccReceivers 密送人
     * @param title 标题
     * @param content 内容
     * @param attachments 附件
     */
    public void sendMany(List<String> receivers, List<String> ccReceivers, List<String> bccReceivers, String title, String content, List<URL> attachments) {
        this.send(receivers, ccReceivers, bccReceivers, title, content, attachments);
    }

    /**
     * 发送邮件
     * @param receivers 接收人
     * @param ccReceivers 抄送人
     * @param bccReceivers 密送人
     * @param title 邮件标题
     * @param content 邮件内容
     * @param attachments 附件信息
     */
    private void send(List<String> receivers, List<String> ccReceivers, List<String> bccReceivers, String title, String content, List<URL> attachments) {
        Assert.state(!CollectionUtils.isEmpty(receivers), "接收人不能为空");
        try {
            MimeMessage message = new MimeMessage(this.session);
            message.setFrom(new InternetAddress(this.username));
            message.setSentDate(DateUtil.getNowDate());
            //接收人
            Address[] address = new Address[receivers.size()];
            for (int i = 0; i < receivers.size(); ++i) {
                address[i] = new InternetAddress(receivers.get(i));
            }
            message.addRecipients(Message.RecipientType.TO, address);
            //抄送人
            if (!CollectionUtils.isEmpty(ccReceivers)) {
                Address[] ccAddress = new Address[ccReceivers.size()];
                for (int i = 0; i < ccReceivers.size(); ++i) {
                    ccAddress[i] = new InternetAddress(ccReceivers.get(i));
                }
                message.addRecipients(Message.RecipientType.CC, ccAddress);
            }
            //密送人
            if (!CollectionUtils.isEmpty(bccReceivers)) {
                Address[] bccAddress = new Address[bccReceivers.size()];
                for (int i = 0; i < bccReceivers.size(); ++i) {
                    bccAddress[i] = new InternetAddress(bccReceivers.get(i));
                }
                message.addRecipients(Message.RecipientType.BCC, bccAddress);
            }
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
                transport.sendMessage(message, message.getAllRecipients());
            } else {
                Transport.send(message, message.getAllRecipients());
            }
        } catch (Exception e) {
            throw new ServiceException(e);
        } finally {
            if (transport != null) {
                try {
                    transport.close();
                } catch (MessagingException ignored) {
                }
            }
        }
    }
}
