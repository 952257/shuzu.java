package com.springboot.web.contoller;

import com.springboot.web.common.CommonResult;
import com.springboot.web.common.ServiceException;
import com.springboot.web.common.ServiceExceptionEnum;
import com.springboot.web.common.po.Person;
import com.springboot.web.dto.PersonDto;
import com.springboot.web.vo.PersonVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

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

    @PostMapping
    public CommonResult<Void> addOne(@RequestBody PersonDto personDto){
        log.info("personDto is  {}",personDto);
        Person person = new Person();
        BeanUtils.copyProperties(personDto,person);
        //stream生成个随机整数
        Random random = new Random();
        person.setId(random.nextLong());
        log.info("person is {}",person);
        list.add(person);
        return new CommonResult<>();
    }

    @PutMapping("/{id}")
    public CommonResult<Void> updateOne(@PathVariable Long id,
                                        @RequestBody PersonDto personDto){
        log.info("id is {}", id);
        log.info("personDto is {}",personDto);
        list.stream().filter(p->p.getId().equals(id))
                .findFirst().ifPresent(po->{
                    po.setId(id);
                    po.setName(personDto.getName());
                    po.setHeight(personDto.getHeight());
                    po.setSex(personDto.getSex());
                    po.setBirthday(personDto.getBirthday());
                });
        log.info("list is {}", list);
        return new CommonResult<>();
    }

    @DeleteMapping("/{id}")
    public CommonResult<Void> deleteOne(@PathVariable Long id) {
        log.info("id is {}", id);
        list.removeIf(p->p.getId().equals(id));
        log.info("list is {}", list);
        return new CommonResult<>();
    }

    @GetMapping("/queryByCondition")
    public CommonResult<List<PersonVo>> queryByCondition(
            @RequestParam(required = false)
            String name,
            @RequestParam(required = false)
            Integer sex,
            @RequestParam
            int curPage,
            @RequestParam
            int pageSize){
        log.info("name is {}", name);
        log.info("sex is {}", sex);
        log.info("curPage is {}", curPage);
        log.info("pageSize is {}", pageSize);
        CommonResult<List<PersonVo>> commonResult = new CommonResult<>();
        List<PersonVo> listVo = list.stream().map(po -> {
            PersonVo vo = new PersonVo();
            BeanUtils.copyProperties(po, vo);
            return vo;
        }).toList();
        commonResult.setData(listVo);
        return commonResult;
    }
}
