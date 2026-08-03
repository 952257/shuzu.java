package com.springboot.web.contoller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@Slf4j
public class TestParamController {

    //web接口
    @GetMapping("/test1")//后端web接口
    public String testParam1(Integer id,
                             String name,
                             Boolean gender,
                             @DateTimeFormat(pattern = "yyyy-MM-dd")
                             Date birth) {
        log.info("test param1");
        log.info("id:" + id);
        log.info("name:" + name);
        log.info("gender:" + gender);
        log.info("birth:" + birth);
        return "成功了";
    }
}