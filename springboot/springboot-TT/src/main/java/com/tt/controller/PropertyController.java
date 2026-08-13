package com.tt.controller;

import com.tt.common.PageResult;
import com.tt.common.Result;
import com.tt.po.Store;
import com.tt.service.PropertyService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/app")
public class PropertyController {

    @Resource
    private PropertyService propertyService;

    @GetMapping("/property.listProperty")
    public PageResult<Store> list(@RequestParam(required = false) String storeId,
                                  @RequestParam(required = false) String name,
                                  @RequestParam(required = false) String tel,
                                  @RequestParam(required = false) Integer page,
                                  @RequestParam(required = false) Integer row) {
        return propertyService.listProperty(storeId, name, tel, page, row);
    }

    @PostMapping("/property.saveProperty")
    public Result<String> save(@RequestBody Store store) {
        return Result.ok(propertyService.saveProperty(store));
    }

    @PostMapping("/property.updateProperty")
    public Result<Void> update(@RequestBody Store store) {
        propertyService.updateProperty(store);
        return Result.ok();
    }

    @PostMapping("/property.deleteProperty")
    public Result<Void> delete(@RequestBody Map<String, String> body) {
        propertyService.deleteProperty(body.get("storeId"));
        return Result.ok();
    }
}
