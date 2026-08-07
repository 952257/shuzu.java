package com.springboot.sakila.service;

import com.github.pagehelper.PageInfo;
import com.springboot.sakila.dto.FilmDto;
import com.springboot.sakila.vo.FilmVo;

import java.util.List;

public interface FilmService {

     void addFilm(FilmDto filmDto);

      void modFilm(Long id,FilmDto filmDto);

      void delFilm(Long id);

      FilmVo queryOne(Long id);

      List<FilmVo> queryAll();

      PageInfo<FilmVo> queryForPage(String title, Integer year,
                                    int pageNum, int pageSize,
                                    String orderBy,String order);

      PageInfo<FilmVo> queryFilmAndActorNamesByCondition(String title,Integer year,int pageNum, int pageSize,
                                                         String orderBy,String order);

}
