package com.springboot.others;

import com.springboot.others.mail.IMailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

@SpringBootTest
public class SendemailApplicationTests {

      /**
     * 注入发送邮件的接口
     */
    @Autowired
    private IMailService mailService;

    /**
     * 测试发送文本邮件
     */
    @Test
    public void sendmailTest() {
        mailService.sendSimpleMail("2318600486@qq.com","主题：你好普通邮件","内容：第一封邮件");
    }

    @Test
    public void sendmailHtmlTest() throws Exception {
        mailService.sendHtmlMail("2318600486@qq.com","主题：你好html邮件","<h1>内容：第一封html邮件</h1>");
    }

    @Test
    public void sendAttachmentsMailTest() throws Exception {
        mailService.sendAttachmentsMail("2318600486@qq.com","主题：你好，有附件",
                "<h1>内容：第一封附件邮件</h1>","/Users/shenxuan/Downloads/壁纸/wyc.jpg");
    }

    @Test
    public void sendInlineMailTest() throws Exception {
        String content =  "<html>" +
                "<body>" +
                "<div><img src=\"cid:img1\" width=\"800\" height=\"500\"></div>" +
                //"<div><img src=\"cid:img2\" width=\"800\" height=\"500\"></div>" +
                "</body></html>";
        HashMap<String, String> imgMap = new HashMap<>();
        imgMap.put("img1","/Users/shenxuan/Downloads/壁纸/wyc.jpg");
        //imgMap.put("img2","C:\\Users\\shichen\\OneDrive\\图片\\zdf.jpg");
        mailService.sendInlineMail("2318600486@qq.com","主题：你好，有帅哥",
               content , imgMap);
    }
}