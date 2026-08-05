package com.springboot.sakila.service.impl;


import com.springboot.sakila.dto.FilmDto;
import com.springboot.sakila.mapper.FilmMapper;
import com.springboot.sakila.po.Film;
import com.springboot.sakila.service.FilmService;
import com.springboot.sakila.vo.FilmVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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

    @Override
    public void modFilm(Long id, FilmDto filmDto) {
        Film po = new Film();
        BeanUtils.copyProperties(filmDto, po);
        po.setFilmId(id);
        filmMapper.updateOne(po);
    }

    @Override
    public void delFilm(Long id) {
        filmMapper.deleteOne(id);
    }

    @Override
    public FilmVo queryOne(Long id) {
        Film film = filmMapper.selectOne(id);
        FilmVo filmVo = new FilmVo();
        filmVo.setFilmId(film.getFilmId());
        filmVo.setTitle(film.getTitle());
        filmVo.setLength(film.getLength());
        filmVo.setReleaseYear(film.getReleaseYear());
        filmVo.setDescription(film.getDescription());
        return filmVo;
    }

    @Override
    public List<FilmVo> queryAll() {
        List<Film> poList = filmMapper.selectAll();
        return poList.stream().map(film -> {
            FilmVo filmVo = new FilmVo();
            filmVo.setFilmId(film.getFilmId());
            filmVo.setTitle(film.getTitle());
            filmVo.setLength(film.getLength());
            filmVo.setReleaseYear(film.getReleaseYear());
            filmVo.setDescription(film.getDescription());
            return filmVo;
        }).toList();
    }
}
