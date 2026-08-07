package com.springboot.sakila.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
    public void modFilm(Long id,FilmDto filmDto) {
        Film po = new Film();
        po.setFilmId(id);
        BeanUtils.copyProperties(filmDto, po);
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

    @Override
    public PageInfo<FilmVo> queryForPage(String title, Integer year, int pageNum,
                                         int pageSize,String orderBy,String order) {
        String orderByColumn = orderBy + " " + order;
        Page<Film> poPageInfo = PageHelper.startPage(pageNum, pageSize,
                orderByColumn);
        //专用的可被序列化的对象

        List<Film> poList = filmMapper.queryByCondition(title, year);
        PageInfo<Film> poPage = poPageInfo.toPageInfo();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
//            //序列化
//            String poPageInfoJson = objectMapper.writeValueAsString(poPage);
//            //反序列化
//            PageInfo<FilmVo> voPageInfo = objectMapper.readValue(poPageInfoJson, PageInfo.class);
            PageInfo<FilmVo> voPageInfo = new PageInfo<>();
            List<FilmVo> filmVoList = poPage.getList().stream().map(po -> {
            FilmVo vo = new FilmVo();
            BeanUtils.copyProperties(po, vo);
            return vo;
            }).toList();
            voPageInfo.setList(filmVoList);
            return voPageInfo;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
