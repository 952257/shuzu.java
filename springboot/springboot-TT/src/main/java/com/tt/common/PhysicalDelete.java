package com.tt.common;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class PhysicalDelete {

    @Resource
    private JdbcTemplate jdbcTemplate;

    public int byId(Class<?> entityClass, String id) {
        QueryHelper.requireHasText(id, "ID不能为空");
        TableInfo info = tableInfo(entityClass);
        return jdbcTemplate.update(
                "DELETE FROM `" + info.getTableName() + "` WHERE `" + info.getKeyColumn() + "` = ?", id);
    }

    public int byColumn(Class<?> entityClass, String column, String value) {
        QueryHelper.requireHasText(value, "删除条件不能为空");
        TableInfo info = tableInfo(entityClass);
        boolean allowed = column.equals(info.getKeyColumn())
                || info.getFieldList().stream().map(TableFieldInfo::getColumn).anyMatch(column::equals);
        QueryHelper.require(allowed, "不支持的删除条件");
        return jdbcTemplate.update(
                "DELETE FROM `" + info.getTableName() + "` WHERE `" + column + "` = ?", value);
    }

    private TableInfo tableInfo(Class<?> entityClass) {
        TableInfo info = TableInfoHelper.getTableInfo(entityClass);
        QueryHelper.require(info != null, "不支持的删除对象");
        return info;
    }
}
