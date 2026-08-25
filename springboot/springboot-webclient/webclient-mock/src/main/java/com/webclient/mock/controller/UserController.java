package com.webclient.mock.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webclient.mock.common.CommonResult;
import com.webclient.mock.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    List<UserDto> list = List.of(
        new UserDto(1L, "张三", "zhangsan@example.com"),
                new UserDto(2L, "李四", "lisi@example.com"),
                new UserDto(3L, "王五", "wangwu@example.com"));

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id) {
        log.info("Get user by id :"+id);
        return new UserDto(1L, "张三", "zhangsan@example.com");
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return list;
    }

    @GetMapping("getByEmail")
    public UserDto getUserByEmail(String email) {
        log.info("Get user by email :"+email);
        return list.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    @PostMapping
    public UserDto addUser(@RequestBody UserDto user) {
        log.info("Post user :"+user);
        list = new ArrayList<>(list);
        user.setId(list.size() + 1L);
        list.add(user);
        return user;
    }

    @PutMapping("/{id}")
    public CommonResult<Void> updateUser(@PathVariable Long id, @RequestBody UserDto user) {
        log.info("Put user :"+user);
        list = new ArrayList<>(list);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(user.getId())) {
                list.set(i, user);
                break;
            }
        }
        return new CommonResult<>(0, "更新成功", null);
    }

    @DeleteMapping("/{id}")
    public CommonResult<Void> deleteUser(@PathVariable Long id) {
        log.info("Delete user by id :"+id);
        list = new ArrayList<>(list);
        list.removeIf(user -> user.getId().equals(id));
        return new CommonResult<>(0, "删除成功", null);
    }

    @GetMapping("/queryForPages")
    public Page<UserDto> queryForPages(@RequestParam(required = false) String username,
                              @RequestParam(required = false) String email,
                              @RequestParam int pageNum, @RequestParam int pageSize) {
        log.info("Query for pages :"+username+" "+email+" "+pageNum+" "+pageSize);
        Page<UserDto> page = new Page<>(pageNum, pageSize);
        page.setRecords(list);
        return page;
    }

    @GetMapping("/profile")
    public CommonResult getProfile(@RequestHeader String Authorization,
                                   @RequestHeader(value = "X-API-Key", required = false) String apiKey) {
        log.info("Authorization :"+Authorization);
        log.info("apiKey :"+apiKey);
        return new CommonResult();
    }

    @GetMapping("/subscribe")
    public CommonResult subscribe() throws InterruptedException {
        log.info("Subscribe start");
        TimeUnit.SECONDS.sleep(5);
        log.info("Subscribe end");
        return new CommonResult();
    }

    @GetMapping("/retry/{id}")
    public UserDto retry(@PathVariable Long id) throws InterruptedException {
        log.info("Retry start, id={}",id);
        System.out.println(1/0);
//        TimeUnit.SECONDS.sleep(8);
        return new UserDto(1L, "张三", "zhangsan@example.com");

    }

    @PostMapping("/upload")
    @ResponseBody
    public String upload(
            @RequestParam("info") String info,
            @RequestPart("user") String userInfoJson,
            @RequestPart("file") MultipartFile file) throws IOException {
        log.info("Upload info :"+info);
        //解析json文本得到对象
        ObjectMapper mapper = new ObjectMapper();
        UserDto user = mapper.readValue(userInfoJson, UserDto.class);
        log.info("Upload user :"+user);
        log.info("Upload file :"+file.getOriginalFilename());
        String fileName = file.getOriginalFilename();
        String filePath = "D:\\upload" + File.separator+fileName;

        //保存的路径
        File dest = new File(filePath);
        //保存上传的文件
        file.transferTo(dest);
        return "Upload file success : " + dest.getAbsolutePath();
    }

    @GetMapping("/download")
    public void download(String fileName, HttpServletResponse response) throws IOException {
        log.info("Download file :"+fileName);
        //文件的完整路径
        String real_path = "D:\\upload"+File.separator+fileName;

        //设置响应头  告知浏览器，要以附件的形式保存内容   filename=浏览器显示的下载文件名
        response.setHeader("content-disposition","attachment;filename="+fileName);

        //读取目标文件，写出给客户端
        IOUtils.copy(new FileInputStream(real_path), response.getOutputStream());

    }
}
