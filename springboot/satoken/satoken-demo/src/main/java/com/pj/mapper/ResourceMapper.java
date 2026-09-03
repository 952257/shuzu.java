package com.pj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pj.entity.Resource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ResourceMapper extends BaseMapper<Resource> {

    @Select("SELECT r.path FROM resource r INNER JOIN user_res ur ON r.id = ur.res_id WHERE ur.user_id = #{userId}")
    List<String> selectPermissionPathsByUserId(@Param("userId") Integer userId);
}
