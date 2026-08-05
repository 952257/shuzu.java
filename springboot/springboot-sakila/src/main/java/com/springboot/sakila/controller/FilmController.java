package com.springboot.sakila.controller;

import com.springboot.sakila.common.CommonResult;
import com.springboot.sakila.dto.FilmDto;
import com.springboot.sakila.service.FilmService;
import com.springboot.sakila.vo.FilmVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("film")
@Slf4j
public class FilmController {

    @Resource
    private FilmService filmService;
    @PostMapping
    public CommonResult<Void> addFilm(@RequestBody FilmDto filmDto) {
        log.info("film is {}", filmDto);
        filmService.addFilm(filmDto);
        return new CommonResult<>();
    }

    @PutMapping("{id}")
    public CommonResult<Void> addFilm(@PathVariable Long id, @RequestBody FilmDto filmDto) {
        log.info("film is {}", filmDto);
        filmService.modFilm(id, filmDto);
        return new CommonResult<>();
    }

    @DeleteMapping("{id}")
    public CommonResult<Void> deleteFilm(@PathVariable Long id) {
        log.info("id is {}", id);
        filmService.delFilm(id);
        return new CommonResult<>();
    }

    @GetMapping("{id}")
    public CommonResult<FilmVo> queryFilmById(@PathVariable Long id) {
        log.info("id is {}", id);
        FilmVo vo = filmService.queryOne(id);
        CommonResult<FilmVo> result = new CommonResult<>();
        result.setData(vo);
        return result;
    }

    @GetMapping()
    public CommonResult<List<FilmVo>> queryAll() {
        List<FilmVo> vo = filmService.queryAll();
        CommonResult<List<FilmVo>> result = new CommonResult<>();
        result.setData(vo);
        return result;
    }


}
