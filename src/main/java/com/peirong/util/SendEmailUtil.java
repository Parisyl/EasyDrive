package com.peirong.util;

import com.peirong.entity.Email;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.mail.internet.MimeMessage;
import java.io.InputStream;
import java.util.Date;

@Component
public class SendEmailUtil {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("ieep.roon@gmail.com")
    private String sendFrom;

    public  void checkMail(Email mailRequest) {
        Assert.notNull(mailRequest, "邮件请求不能为空");
        Assert.notNull(mailRequest.getSendTo(), "邮件收件人不能为空");
        Assert.notNull(mailRequest.getSubject(), "邮件主题不能为空");
        Assert.notNull(mailRequest.getText(), "邮件收件人不能为空");
    }

    public void sendEmail(String sendTo, String code) {
        Email email = new Email();
        email.setSendTo(sendTo);
        email.setSubject("账户安全代码");
        email.setText("您的验证码为：" + code + "，有效时间为5分钟。");
        checkMail(email);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sendFrom);
        message.setTo(email.getSendTo());
        message.setSubject(email.getSubject());
        message.setText(email.getText());
        message.setSentDate(new Date());
        javaMailSender.send(message);
    }
}
