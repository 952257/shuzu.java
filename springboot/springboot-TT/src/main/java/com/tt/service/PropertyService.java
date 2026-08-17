package com.tt.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tt.common.PhysicalServiceImpl;
import com.tt.common.IdGenerator;
import com.tt.common.PageResult;
import com.tt.common.QueryHelper;
import com.tt.common.UserContext;
import com.tt.mapper.StoreMapper;
import com.tt.po.Store;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class PropertyService extends PhysicalServiceImpl<StoreMapper, Store> {

    public PageResult<Store> listProperty(String storeId, String name, String tel, Integer page, Integer row) {
        LambdaQueryWrapper<Store> wrapper = new LambdaQueryWrapper<>();
        if (!UserContext.isAdmin()) {
            QueryHelper.requireHasText(UserContext.getStoreId(), "未绑定物业公司");
            wrapper.eq(Store::getStoreId, UserContext.getStoreId());
        } else {
            wrapper.eq(StringUtils.hasText(storeId), Store::getStoreId, storeId);
        }
        wrapper.like(StringUtils.hasText(name), Store::getName, name)
                .like(StringUtils.hasText(tel), Store::getTel, tel)
                .orderByDesc(Store::getCreateTime);
        return QueryHelper.toPage(this, wrapper, page, row);
    }

    public String saveProperty(Store store) {
        UserContext.requireAdmin();
        QueryHelper.requireHasText(store.getName(), "物业公司名称不能为空");
        store.setStoreId(IdGenerator.nextId());
        if (!StringUtils.hasText(store.getState())) {
            store.setState("48001");
        }
        save(store);
        return store.getStoreId();
    }

    public void updateProperty(Store store) {
        UserContext.requireAdmin();
        QueryHelper.requireHasText(store.getStoreId(), "物业编号不能为空");
        updateById(store);
    }

    public void deleteProperty(String storeId) {
        UserContext.requireAdmin();
        QueryHelper.requireHasText(storeId, "物业编号不能为空");
        removeById(storeId);
    }
}
