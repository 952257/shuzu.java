package com.springboot.others;

import com.springboot.others.task.AsyncTasks;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
public class AsyncTest {

    @Resource
    private AsyncTasks asyncTasks;
    @Test
    public void test() throws Exception {
        asyncTasks.doTaskOne();
        asyncTasks.doTaskTwo();
        asyncTasks.doTaskThree();

        Thread.sleep(10000);
    }
}
