package com.springboot.others.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.Date;

//@Component
@Slf4j
public class ScheduledTasks {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 5000)
    @Async
    public void reportCurrentTime() {
        log.info("现在时间：" + dateFormat.format(new Date()));
    }
    
   @Scheduled(cron = "0/3 * * * * *")
    public void reportCurrentTime2() {
        log.info("现在时间2：" + dateFormat.format(new Date()));
    }
}