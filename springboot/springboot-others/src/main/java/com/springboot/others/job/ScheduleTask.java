package com.springboot.others.job;

import com.springboot.others.mapper.CronMapper;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import javax.annotation.Resource;

//@Component
public class ScheduleTask implements SchedulingConfigurer {
    @Resource
    private CronMapper cronMapper;
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(
                //添加任务内容
                () -> process(),
                //设置执行的周期
                triggerContext -> {
                    //查询cron表达式
                    String cron = cronMapper.getCron(1L);
                    if (cron.isEmpty()) {
                        System.out.println("cron is null");
                    }
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                });
    }
    private void process() {
        System.out.println("基于接口的定时任务");
    }
}