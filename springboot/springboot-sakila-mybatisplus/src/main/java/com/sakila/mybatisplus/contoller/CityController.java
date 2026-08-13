package com.sakila.mybatisplus.contoller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sakila.mybatisplus.common.CommonResult;
import com.sakila.mybatisplus.dto.CityDto;
import com.sakila.mybatisplus.service.CityService;
import com.sakila.mybatisplus.vo.CityVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("city")
@Slf4j
public class CityController {

    @Resource
    private CityService cityService;

    @PostMapping
    public CommonResult<Void> addOne(@RequestBody CityDto cityDto){
        log.info("cityDto is {}", cityDto);
        cityService.addOne(cityDto);
        return  new CommonResult<>();

    }

    @PutMapping("{id}")
    public CommonResult<Void> modOne(@PathVariable Integer id,
                                     @RequestBody CityDto cityDto){
        log.info("cityDto is {}", cityDto);
        cityDto.setCityId(id);
        cityService.modOne(cityDto);
        return  new CommonResult<>();
    }

    @DeleteMapping("{id}")
    public CommonResult<Void> delOne(@PathVariable Integer id){
        log.info("id is {}", id);
        cityService.delOne(id);
        return  new CommonResult<>();
    }

    @GetMapping("{id}")
    public CommonResult<CityVo> queryOne(@PathVariable Integer id){
        CityVo vo = cityService.queryOne(id);
        CommonResult<CityVo> result = new CommonResult<>();
        result.setData(vo);
        return result;
    }

    @GetMapping("/pages")
    public CommonResult<Page<CityVo>> pages(@RequestParam(required = false) String city,
                                            @RequestParam int current,
                                            @RequestParam int size,
                                            @RequestParam String orderBy,
                                            @RequestParam String order){
        Page<CityVo> pages = cityService.pages(city, current, size, orderBy, order);
        CommonResult<Page<CityVo>> result = new CommonResult<>();
        result.setData(pages);
        return result;
    }

    @GetMapping("/pages2")
    public CommonResult<Page<CityVo>> pages2(@RequestParam(required = false) String city,
                                             @RequestParam(required = false) String country,
                                            @RequestParam int current,
                                            @RequestParam int size,
                                            @RequestParam String orderBy,
                                            @RequestParam String order){
        Page<CityVo> pages = cityService.pages2(city, country,current, size, orderBy, order);
        CommonResult<Page<CityVo>> result = new CommonResult<>();
        result.setData(pages);
        return result;
    }
}
