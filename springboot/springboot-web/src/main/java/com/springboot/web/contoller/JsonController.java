package com.springboot.web.contoller;

import com.springboot.web.dto.UserDto;
import com.springboot.web.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/json")
public class JsonController{    
	@RequestMapping("/test1")
//    @ResponseBody //将handler的返回值，转换成json(jackson),并将json响应给客户端。
    /**
     * {
     *     "id": 1,
     *     "name": "aaa",
     *     "birth": "2026-08-03T07:55:09.289+00:00",
     *     "gender": false
     * }
     */
    public UserVo test1(){
        return new UserVo(1,"aaa",new Date(),false);
    }
    // @ResponseBody还可以用在handler的返回值上

    /**
     * [
     *     {
     *         "id": 1,
     *         "name": "aaa",
     *         "birth": "2026-08-03T08:01:53.142+00:00",
     *         "gender": true
     *     },
     *     {
     *         "id": 2,
     *         "name": "bbb",
     *         "birth": "2026-08-03T08:01:53.142+00:00",
     *         "gender": false
     *     },
     *     {
     *         "id": 3,
     *         "name": "ccc",
     *         "birth": "2026-08-03T08:01:53.142+00:00",
     *         "gender": true
     *     }
     * ]
     * @return
     */
    @GetMapping("/test2")
    public  List<UserVo> test2(){
        return List.of(
                new UserVo(1, "aaa", new Date(), true),
                new UserVo(2, "bbb", new Date(), false),
                new UserVo(3, "ccc", new Date(), true)
        );
    }
    // 如果返回值已经是字符串，则不需要转json，直接将字符串响应给客户端 
    @RequestMapping(value="/test3")
//    @ResponseBody
    public String test3(){
        return "你好";
    }

    @PostMapping("/users")
    public String addUser(@RequestBody UserDto userDto){//@RequestBody将请求体中的json数据转换为java对象
        log.info("Post user :"+userDto);
        return "success";
    }
}