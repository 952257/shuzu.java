package com.springboot.web.common.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@ApiModel(description="用户实体")
@AllArgsConstructor
@NoArgsConstructor
public class User2 {

    @ApiModelProperty("用户编号")
    private Long id;

    @NotNull
    @Size(min = 2, max = 5)
    @ApiModelProperty("用户姓名")
    private String name;

    @NotNull
    @Max(100)
    @Min(10)
    @ApiModelProperty("用户年龄")
    private Integer age;

    @Email
    @ApiModelProperty("用户邮箱")
    private String email;

    public User2(Long id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }
}