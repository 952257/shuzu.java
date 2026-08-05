package com.springboot.sakila.service;

import com.springboot.sakila.dto.FilmDto;
import com.springboot.sakila.vo.FilmVo;

import java.util.List;

public interface FilmService {

    void addFilm(FilmDto filmDto);

    void modFilm(Long id,FilmDto filmDto);

    void delFilm(Long id);

    FilmVo queryOne(Long id);

    List<FilmVo> queryAll();
}
