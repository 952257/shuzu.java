package com.tt.common;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class PageResult<T> extends Result<List<T>> {

    private Integer page = 1;
    private Integer rows = 10;
    private Long records = 0L;
    private Long total = 0L;

    public static <T> PageResult<T> of(List<T> list, long total, int page, int rows) {
        PageResult<T> result = new PageResult<>();
        result.setData(list);
        result.setTotal(total);
        result.setPage(page);
        result.setRows(rows);
        result.setRecords((long) list.size());
        return result;
    }
}
