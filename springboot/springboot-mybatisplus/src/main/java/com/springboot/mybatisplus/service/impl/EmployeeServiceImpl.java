package com.springboot.mybatisplus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springboot.mybatisplus.entity.Employee;
import com.springboot.mybatisplus.mapper.EmployeeMapper;
import com.springboot.mybatisplus.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}