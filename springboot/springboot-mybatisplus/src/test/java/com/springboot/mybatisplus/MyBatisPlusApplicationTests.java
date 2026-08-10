package com.springboot.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springboot.mybatisplus.entity.Employee;
import com.springboot.mybatisplus.mapper.EmployeeMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@Slf4j
public class MyBatisPlusApplicationTests {
    @Autowired
    private EmployeeMapper employeeMapper;
    @Test
    public void testSelect(){
        List<Employee> employeeList = employeeMapper.selectList(null);
		log.info(employeeList.toString());
    }

    @Test
    public void testinsert() {
        Employee employee=new Employee();
        //employee.setEmpId(100000);
        employee.setName("刘龙");
        employee.setEmpGender("男");
        employee.setAge(25);
        employee.setEmail("liulong@163.com");
        employeeMapper.insert(employee);
    }

    @Test
    public void testUpdateById() {
        Employee employee=new Employee();
        employee.setEmpId(2086623511142629378L);
        employee.setName("刘龙2");
        employee.setEmpGender("女");
        employee.setAge(23);
//        employee.setEmail("liulong@163.com");
        employeeMapper.updateById(employee);
    }

    @Test
    public void testUpdateByName(){
        //根据员工的名字,更新
        Employee employee=new Employee();
        employee.setEmpGender("男");
        employee.setAge(18);
        employee.setEmail("liulong@126.com");
        employeeMapper.update(employee,new UpdateWrapper<Employee>().eq("name","刘龙"));
    }

    @Test
    public void testSelectById() {
        Employee employee=employeeMapper.selectById(2086623511142629378L);
        log.info(employee.toString());
    }

    @Test
    public void testSelectBatchIds() {
        List<Long> list= List.of(1367686308726788098L,1367709299695099906L,1367717669156028418L);
        List<Employee> employeeList = employeeMapper.selectBatchIds(list);
        log.info("list is {}", employeeList);
    }

    @Test
    public void testSelectByMap() {
        Map<String,Object> map=new HashMap<>();
        map.put("emp_gender","男");
        map.put("age",28);
        List<Employee> employeeList = employeeMapper.selectByMap(map);
        log.info("list is {}", employeeList);
    }

    @Test
    public void testDeleteById(){
        int rows = employeeMapper.deleteById(2086623511142629378L);
        log.info("受影响的行数:{}",rows);
    }
    @Test
    public void testDeleteBatchIds(){
        List<Long> list= List.of(1L,2L,3L);
        int rows = employeeMapper.deleteBatchIds(list);
        log.info("受影响的行数:{}"+rows);
    }

    @Test
    public void testSelectOne(){
        QueryWrapper<Employee> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("name","何雨柱");
        queryWrapper.eq("emp_gender","男");
        Employee employee = employeeMapper.selectOne(queryWrapper);
        log.info("employee is {}", employee);
    }

    //查询姓名中带有"磊"的并且年龄小于30的员工
    @Test
    public void testSelectList(){
        QueryWrapper<Employee> queryWrapper=new QueryWrapper<>();
        queryWrapper.like("name","磊")
                .lt("age",30);
        List<Employee> employeeList = employeeMapper.selectList(queryWrapper);
        log.info("list is {}", employeeList);
    }

    @Test
    public void testSelectList2(){
        QueryWrapper<Employee> queryWrapper=new QueryWrapper<>();
        queryWrapper
                .like("name","王")
                .or()
                .eq("emp_gender","男")
                .orderByDesc("age");
        List<Employee> employeeList = employeeMapper.selectList(queryWrapper);
        log.info("list is {}", employeeList);
    }

    @Test
    public void testSelectList3(){
        QueryWrapper<Employee> queryWrapper=new QueryWrapper<>();
        queryWrapper.likeRight("name","刘")
                .and(wq->wq.lt("age",35)
                        .or().isNotNull("email"));
        List<Employee> employeeList = employeeMapper.selectList(queryWrapper);
        log.info("list is {}", employeeList);
    }

    @Test
    //动态条件查询
    public void testSelectList4(){
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>(Employee.class);
        Employee employee = new Employee();
        employee.setName("雨");
//        employee.setEmpGender("男");
        queryWrapper.func(i->{
            if(employee.getName() != null)
                i.like("name",employee.getName());
            if(employee.getEmpGender() != null)
                i.eq("emp_gender",employee.getEmpGender());
        });
        List<Employee> employees = employeeMapper.selectList(queryWrapper);
        log.info("employees is {}", employees);
    }

    @Test
    public void testSelectPage(){
        QueryWrapper<Employee> queryWrapper=new QueryWrapper<>();
        queryWrapper.lt("age",500);
        Page<Employee> page=new Page<>(1,10);
        Page<Employee> employeePage = employeeMapper.selectPage(page, queryWrapper);
        log.info("当前页:{}", employeePage.getCurrent());
        log.info("每页记录数:{}",employeePage.getSize());
        log.info("总记录数:{}",employeePage.getTotal());
        log.info("总页数:{}",employeePage.getPages());
        List<Employee> employeeList = employeePage.getRecords();
        log.info("list is {}", employeeList);
    }
}