package com.tt.common;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.util.StringUtils;

public class QueryHelper {

    public static int page(Integer page) {
        return page == null || page < 1 ? 1 : page;
    }

    public static int row(Integer row) {
        return row == null || row < 1 ? 10 : row;
    }

    public static <T> PageResult<T> toPage(IService<T> service, Wrapper<T> wrapper, Integer page, Integer row) {
        int p = page(page);
        int r = row(row);
        Page<T> mp = service.page(new Page<>(p, r), wrapper);
        return PageResult.of(mp.getRecords(), mp.getTotal(), p, r);
    }

    public static void require(boolean condition, String msg) {
        if (!condition) {
            throw new ServiceException(ServiceExceptionEnum.PARAM_ERROR.getCode(), msg);
        }
    }

    public static void requireHasText(String value, String msg) {
        require(StringUtils.hasText(value), msg);
    }

    public static void requireCommunityId(String communityId) {
        requireHasText(communityId, "小区ID不能为空");
    }
}
