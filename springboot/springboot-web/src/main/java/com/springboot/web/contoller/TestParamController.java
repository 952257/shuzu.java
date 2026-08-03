package com.springboot.web.contoller;

import com.springboot.web.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/test2")
    public String testParam2(UserDto userDto){
        log.info("test param2");
        log.info("user:"+userDto);
        return "success";
    }

    // http://127.0.0.1:8080/rest/1  {id}匹配到1
// 路径名和参数名相同则@PathVariable("id")可简写为 @PathVariable
// @PathVariable将{id}路径匹配到值赋给id参数
    @GetMapping("/rest/{id}")
    public String testParam3(@PathVariable Integer id){
        log.info("id:"+id);
        return "success";
    }


}