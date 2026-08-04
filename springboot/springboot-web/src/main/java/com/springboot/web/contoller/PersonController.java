package com.springboot.web.contoller;

import com.springboot.web.common.CommonResult;
import com.springboot.web.common.ServiceException;
import com.springboot.web.common.ServiceExceptionEnum;
import com.springboot.web.common.po.Person;
import com.springboot.web.vo.PersonVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/person")
@Slf4j
public class PersonController {
    //假装是数据库的数据
    List<Person> list = new ArrayList<>();

    {
        list.add(new Person(111L, "aaa", 160, 0, new Date()));
        list.add(new Person(222L, "bbb", 170, 1, new Date()));
        list.add(new Person(333L, "ccc", 180, 1, new Date()));
    }
    @GetMapping("/{id}")
    public CommonResult<PersonVo> queryById(@PathVariable Long id){
        log.info("id = "+id);
        List<Person> list2 = list.stream()
                .filter(p -> p.getId().equals(id))
                .toList();
        if(list2.isEmpty())
            throw new ServiceException(ServiceExceptionEnum.PERSON_NOT_EXIST);
        Person person = list2.get(0);
        PersonVo personVo = new PersonVo();
        BeanUtils.copyProperties(person, personVo);

        CommonResult<PersonVo> result = new CommonResult<>();
        result.setData(personVo);
        return result;
    }
}
