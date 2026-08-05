package com.springboot.sakila.controller;

import com.springboot.sakila.common.CommonResult;
import com.springboot.sakila.dto.FilmDto;
import com.springboot.sakila.service.FilmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
}
