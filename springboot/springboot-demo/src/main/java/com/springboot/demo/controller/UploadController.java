package com.springboot.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Controller
@Slf4j
public class UploadController {

      @Value("${file.upload.path}")
    private String path;

    @GetMapping("/")
    public String uploadPage() {
        return "upload";
    }

    @PostMapping("/upload")
    @ResponseBody
    public String upload(@RequestPart MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String filePath = path + File.separator+fileName;

        File dest = new File(filePath);
        file.transferTo(dest);
        return "Upload file success : " + dest.getAbsolutePath();
    }

    @RequestMapping("/download")
    public void download(String name, HttpServletResponse response) throws IOException {
        System.out.println("name:"+name);
        //文件的完整路径
        String real_path = path+"\\"+name;

        //设置响应头  告知浏览器，要以附件的形式保存内容   filename=浏览器显示的下载文件名
        response.setHeader("content-disposition","attachment;filename="+name);

        //读取目标文件，写出给客户端
        IOUtils.copy(new FileInputStream(real_path), response.getOutputStream());

    }
}