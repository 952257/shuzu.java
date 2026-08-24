package com.springboot.redis;

import com.springboot.redis.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SpringBootTest
public class RedisTemplateTest {

    @Resource
    private RedisTemplate<String,Object> template;

    @Test
    public void testString(){
//        template.opsForValue().set("username", "aaa");
        Customer customer = new Customer(1002, 1,
                "ccc", "bbb", "qqq@www.com",
                1, 1, new Date());
        //序列化
//        template.opsForValue().set("customer:"+customer.getCustomerId()
//        , customer);
        //反序列化
        Customer o = (Customer) template.opsForValue().get("customer:1002");
        System.out.println(o);;

    }

    @Test
    public void testList(){
        List<Customer> list = List.of(new Customer(1005, 1,
                        "ccc", "bbb", "qqq@www.com",
                        1, 1, new Date()),
                new Customer(1006, 1,
                        "ccc", "bbb", "qqq@www.com",
                        1, 1, new Date()),
                new Customer(1007, 1,
                        "ccc", "bbb", "qqq@www.com",
                        1, 1, new Date())
        );
//        template.opsForValue().set("customer:list", customers);
        template.opsForList().leftPushAll("customer:list2", list.toArray());
        List<Object> range = template.opsForList().range("customer:list2", 0, -1);
        System.out.println(range);
    }

    @Test
    public void testSet(){
        Set<Customer> set = Set.of(new Customer(1005, 1,
                        "ccc", "bbb", "qqq@www.com",
                        1, 1, new Date()),
                new Customer(1006, 1,
                        "ccc", "bbb", "qqq@www.com",
                        1, 1, new Date()),
                new Customer(1007, 1,
                        "ccc", "bbb", "qqq@www.com",
                        1, 1, new Date())
        );
        template.opsForSet().add("customer:set", set.toArray());
        Set<Object> members = template.opsForSet().members("customer:set");
        System.out.println(members);
    }

    @Test
    public void testHash(){
        List<Customer> list = List.of(new Customer(1005, 1,
                        "ccc", "bbb", "qqq@www.com",
                        1, 1, new Date()),
                new Customer(1006, 1,
                        "ccc", "bbb", "qqq@www.com",
                        1, 1, new Date()),
                new Customer(1007, 1,
                        "ccc", "bbb", "qqq@www.com",
                        1, 1, new Date())
        );
        list.forEach(customer -> template.opsForHash().put("customer:hash",
                customer.getCustomerId().toString(), customer));
        Map<Object, Object> customerMap = template.opsForHash().entries("customer:hash");
        System.out.println(customerMap);
    }

    @Test
    public void testZSet(){
        Set<Customer> set = Set.of(new Customer(1005, 1,
                        "ccc", "bbb", "qqq@www.com",
                        1, 1, new Date()),
                new Customer(1006, 1,
                        "ccc", "bbb", "qqq@www.com",
                        1, 1, new Date()),
                new Customer(1007, 1,
                        "ccc", "bbb", "qqq@www.com",
                        1, 1, new Date())
        );
        set.forEach(customer -> template.opsForZSet()
                .add("customer:zset", customer,customer.getCustomerId()));
        //LinkedHashSet
        Set<Object> range = template.opsForZSet().reverseRange("customer:zset", 0, -1);
        System.out.println(range);
        Set<ZSetOperations.TypedTuple<Object>> typedTuples = template.opsForZSet().rangeWithScores("customer:zset", 0, -1);
        typedTuples.forEach(typedTuple->{
            System.out.println("-----------------------");
            System.out.println(typedTuple.getValue());
            System.out.println(typedTuple.getScore());
        });
    }

}
