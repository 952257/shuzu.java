package com.springboot.others.mail;

import java.util.Map;

public interface IMailService {

    /**
     * 发送文本邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     */
    void sendSimpleMail(String to, String subject, String content) ;

    /**
     * 发送HTML邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     */
     void sendHtmlMail(String to, String subject, String content) throws Exception;


    /**
     * 嵌入图片
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     * @param imgMap 嵌入的背景图片
     */

    void sendInlineMail(String to, String subject, String content, Map<String,String> imgMap) throws Exception;

    /**
     * 发送带附件的邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     * @param filePath 附件
     */
     void sendAttachmentsMail(String to, String subject, String content, String filePath) throws Exception;


}