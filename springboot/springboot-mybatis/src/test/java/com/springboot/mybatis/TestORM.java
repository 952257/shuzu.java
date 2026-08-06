package com.springboot.mybatis;

import com.springboot.mybatis.entity.Manager;
import com.springboot.mybatis.entity.ManagerCamel;
import com.springboot.mybatis.entity.Passenger;
import com.springboot.mybatis.entity.Passport;
import com.springboot.mybatis.mapper.ManagerMapper;
import com.springboot.mybatis.mapper.PassengerMapper;
import com.springboot.mybatis.mapper.PassportMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
@Slf4j
public class TestORM {

    @Resource
    private ManagerMapper managerMapper;

    @Test
    public void testSelectManagerById(){
        Manager manager = managerMapper.selectManagerById(1001);
        log.info("manager is {}", manager);
    }

    @Test
    public void testSelectManagerCamelById(){
        ManagerCamel manager = managerMapper.selectManagerCamelById(1001);
        log.info("manager is {}", manager);
    }

    @Resource
    private PassengerMapper passengerMapper;
    @Test
    public void testSelectPassengerById(){
        Passenger passenger = passengerMapper.selectPassengerById(1001);
        log.info("passenger is {}", passenger);
        Passport passport = passenger.getPassport();
    }

    @Test
    public void testSelectPassengerById2(){
        Passenger passenger = passengerMapper.selectPassengerById2(1001);
        log.info("passenger is {}", passenger);
    }

    @Test
    public void testSelectAllPassengers(){
        List<Passenger> list = passengerMapper.selectAllPassengers();
        log.info("list is {}", list);
    }

    @Resource
    private PassportMapper passportMapper;

    @Test
    public void testSelectPassportById(){
        Passport passport = passportMapper.selectPassportById(1000001);
        log.info("passport is {}", passport);

    }

    @Test
    public void testSelectPassportById2(){
        Passport passport = passportMapper.selectPassportById2(1000001);
        log.info("passport is {}", passport);

    }
}
