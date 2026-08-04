package com.springboot.web.contoller;

import com.springboot.web.common.po.User2;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Api(tags = "用户管理")
@RestController
@RequestMapping(value = "/users")     // 通过这里配置使下面的映射都在/users下
public class User2Controller {

    // 创建线程安全的Map，模拟users信息的存储
    static Map<Long, User2> users = new ConcurrentHashMap<>();
    {
        users.put(1L, new User2(1L, "aaa", 20));
        users.put(2L, new User2(2L, "bbb", 21));
        users.put(3L, new User2(3L, "ccc", 19));
    }

    @GetMapping
    @ApiOperation(value = "获取用户列表")
    public List<User2> getUser2List() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    @ApiOperation(value = "创建用户", notes = "根据User2对象创建用户")
    public String postUser2(@Valid @RequestBody User2 user) {
        users.put(user.getId(), user);
        return "success";
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "获取用户详细信息", notes = "根据url的id来获取用户详细信息")
    public User2 getUser2(@PathVariable Long id) {
        return users.get(id);
    }

    @PutMapping("/{id}")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "用户编号", required = true, example = "1")
    @ApiOperation(value = "更新用户详细信息", notes = "根据url的id来指定更新对象，并根据传过来的user信息来更新用户详细信息")
    public String putUser2(@PathVariable Long id, @RequestBody User2 user) {
        User2 u = users.get(id);
        u.setName(user.getName());
        u.setAge(user.getAge());
        users.put(id, u);
        return "success";
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除用户", notes = "根据url的id来指定删除对象")
    public String deleteUser2(@PathVariable Long id) {
        users.remove(id);
        return "success";
    }

}