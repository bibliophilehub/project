package com.inext.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class EmailUtils {

    private static Logger logger = LoggerFactory.getLogger(EmailUtils.class);

    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * 邮件发送
     *
     * @param from    发件人
     * @param to      收件人
     * @param cc      抄送人
     * @param subject 标题
     * @param text    内容
     */
    public void send(String from, String to, String cc, String subject, String text) {
        String tos[] = null;
        if (to != null && !"".equals(to)) {
            tos = to.split(",");
        }
        String ccs[] = null;
        if (cc != null && !"".equals(cc)) {
            ccs = cc.split(",");
        }
        send(from, tos, ccs, subject, text, false);
    }

    /**
     * 邮件发送
     *
     * @param from    发件人
     * @param to      收件人
     * @param cc      抄送人
     * @param subject 标题
     * @param text    内容
     * @param isHtml  内容是否为html
     */
    public void send(String from, String to, String cc, String subject, String text, boolean isHtml) {
        String tos[] = null;
        if (to != null && !"".equals(to)) {
            tos = to.split(",");
        }
        String ccs[] = null;
        if (cc != null && !"".equals(cc)) {
            ccs = cc.split(",");
        }
        send(from, tos, ccs, subject, text, isHtml);
    }

    /**
     * 邮件发送
     *
     * @param from    发件人
     * @param to      收件人
     * @param cc      抄送人
     * @param subject 标题
     * @param text    内容
     * @param isHtml  内容是否为html
     */
    public void send(String from, String to, String cc, String subject, String text, boolean isHtml, File file) {
        String tos[] = null;
        if (to != null && !"".equals(to)) {
            tos = to.split(",");
        }
        String ccs[] = null;
        if (cc != null && !"".equals(cc)) {
            ccs = cc.split(",");
        }
        send(from, tos, ccs, subject, text, isHtml, file);
    }

    /**
     * 邮件发送
     *
     * @param from     发件人
     * @param to       收件人
     * @param cc       抄送人
     * @param subject  标题
     * @param text     内容
     * @param isHtml   内容是否为html
     * @param filePath 文件地址
     */
    public void send(String from, String to, String cc, String subject, String text, boolean isHtml, String filePath) {
        File file = new File(filePath);
        String tos[] = null;
        if (to != null && !"".equals(to)) {
            tos = to.split(",");
        }
        String ccs[] = null;
        if (cc != null && !"".equals(cc)) {
            ccs = cc.split(",");
        }
        send(from, tos, ccs, subject, text, isHtml, file);
    }

    /**
     * 邮件发送
     *
     * @param from    发件人
     * @param to      收件人
     * @param cc      抄送人
     * @param subject 标题
     * @param text    内容
     * @param isHtml  内容是否为html
     */
    public void send(String from, String[] to, String[] cc, String subject, String text, boolean isHtml) {
        List<File> files = new ArrayList<>();
        send(from, to, cc, subject, text, isHtml, files);
    }

    /**
     * 邮件发送
     *
     * @param from     发件人
     * @param to       收件人
     * @param cc       抄送人
     * @param subject  标题
     * @param text     内容
     * @param isHtml   内容是否为html
     * @param filePath 文件地址
     */
    public void send(String from, String[] to, String[] cc, String subject, String text, boolean isHtml, String filePath) {
        File file = new File(filePath);
        send(from, to, cc, subject, text, isHtml, file);
    }

    /**
     * 邮件发送
     *
     * @param from    发件人
     * @param to      收件人
     * @param cc      抄送人
     * @param subject 标题
     * @param text    内容
     * @param isHtml  内容是否为html
     * @param file    文件
     */
    public void send(String from, String[] to, String[] cc, String subject, String text, boolean isHtml, File file) {
        List<File> files = new ArrayList<>();
        if (file != null) {
            files.add(file);
        }
        send(from, to, cc, subject, text, isHtml, files);
    }

    /**
     * 邮件发送
     *
     * @param from    发件人
     * @param to      收件人
     * @param cc      抄送人
     * @param subject 标题
     * @param text    内容
     * @param isHtml  内容是否为html
     * @param files   文件集
     */
    public void send(String from, String to, String cc, String subject, String text, boolean isHtml, List<File> files) {
        String tos[] = null;
        if (to != null && !"".equals(to)) {
            tos = to.split(",");
        }
        String ccs[] = null;
        if (cc != null && !"".equals(cc)) {
            ccs = cc.split(",");
        }
        send(from, tos, ccs, subject, text, isHtml, files);
    }

    /**
     * 邮件发送
     *
     * @param from    发件人
     * @param to      收件人
     * @param cc      抄送人
     * @param subject 标题
     * @param text    内容
     * @param isHtml  内容是否为html
     * @param files   文件集
     */
    public void send(String from, String[] to, String[] cc, String subject, String text, boolean isHtml, List<File> files) {
        MimeMessage message = null;
        try {
            if (from == null || "".equals(from)) {
                logger.info("发件人为空!");
                return;
            }

            if (to == null || to.length == 0) {
                logger.info("收件人为空!");
                return;
            }
            message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true); //这里创建的是MimeMessageHelper

            helper.setFrom(from);
            helper.setTo(to);
            if (cc != null && !"".equals(cc)) {
                helper.setCc(cc);
            }
            helper.setSubject(subject);
            helper.setText(text, isHtml); //且在调用setText时需要在第二个参数传入true，这样才会使用HTML格式发送邮件

            if (files != null && files.size() > 0) {
                for (File file : files) {
                    FileSystemResource fileSystemResource = new FileSystemResource(file);
                    helper.addAttachment(fileSystemResource.getFilename(), fileSystemResource);
                }
            }
            javaMailSender.send(message);
        } catch (MessagingException e) {
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }

}
