package com.springboot.sakila.service.impl;


import com.springboot.sakila.dto.FilmDto;
import com.springboot.sakila.mapper.FilmMapper;
import com.springboot.sakila.po.Film;
import com.springboot.sakila.service.FilmService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service//业务层组件
public class FilmServiceImpl implements FilmService {

    @Resource
    private FilmMapper filmMapper;
    @Override
    public void addFilm(FilmDto filmDto) {
        Film po = new Film();
        BeanUtils.copyProperties(filmDto, po);
        filmMapper.insertOne(po);
    }
}
