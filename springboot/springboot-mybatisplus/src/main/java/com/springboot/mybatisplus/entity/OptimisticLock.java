package com.springboot.mybatisplus.entity;

import com.baomidou.mybatisplus.annotation.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OptimisticLock {

    private int id;

    private String name;

    @Version
    private int version;
}