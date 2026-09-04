package com.zhrj.exam.configuration;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@org.springframework.context.annotation.Profile("druid")
public class DruidDataSourceConfig {

    //编写方法,注入DruidDataSource
    //为什么我们注入自己的DataSource , 默认的HiKariDatasource失效?
    //1. 默认的数据源是如配置? @ConditionalOnMissingBean({ DataSource.class, XADataSource.class })
    //   解读通过@ConditionalOnMissingBean({ DataSource.class}) 判断如果容器有DataSource Bean 就不注入默认的HiKariDatasource
    @ConfigurationProperties("spring.datasource")
    @Bean
    public DataSource dataSource() {
        //1. 配置了 @ConfigurationProperties("spring.datasource")
        //   就可以读取到application.yml的配置，注意：我们需要将bean注入到spring ioc容器中、bean中提供get\set方法
        //2. 我们就不需要调用DruidDataSource 对象的setXxx, 会自动关联

        DruidDataSource druidDataSource = new DruidDataSource();
        //druidDataSource.setUrl();
        //druidDataSource.setUsername();
        //druidDataSource.setPassword();
        return druidDataSource;
    }

}
