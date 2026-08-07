package com.springboot.mybatis;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.springboot.mybatis.entity.Passenger;
import com.springboot.mybatis.entity.Passport;
import com.springboot.mybatis.mapper.PassengerMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@SpringBootTest
@Slf4j
public class TestDynamicSql {

    @Resource
    private PassengerMapper passengerMapper;

    @Test
    public void testSelectPassengerByCondition(){
        Passenger condition = new Passenger();
        condition.setSex("M");
//        condition.setName("ng");
        List<Passenger> passengers = passengerMapper.selectPassengerByCondition(condition);
        log.info("passengers is {}", passengers);
    }

    @Test
    public void testSelectPassengerByCondition2(){
        Passenger condition = new Passenger();
        condition.setName("ng");
        Passport passport = new Passport();
        passport.setNationality("China");
        condition.setPassport(passport);

        List<Passenger> passengers = passengerMapper.selectPassengerByCondition2(condition);
        log.info("passengers is {}", passengers);
    }

    @Test
    public void testUpdatePassengerIfNecessary(){
        Passenger passenger = new Passenger();
        passenger.setId(1003);
        passenger.setSex("M");
        passenger.setName("QQQ");
        passenger.setBirthday(new Date());
        passengerMapper.updatePassengerIfNecessary(passenger);
    }

    @Test
    public void testSelectPassengersIn(){
        List<Passenger> passengers = passengerMapper.selectPassengersIn(1001, 1002, 1003);
        log.info("passengers is {}", passengers);
    }

    @Test
    public void testSelectAllPassengersBase(){
        Page<Passenger> pageInfo = PageHelper.startPage(1, 10);
        List<Passenger> passengers = passengerMapper.selectAllPassengersBase();
        log.info("pageInfo is {}", pageInfo);
        List<Passenger> result = pageInfo.getResult();
    }
}
