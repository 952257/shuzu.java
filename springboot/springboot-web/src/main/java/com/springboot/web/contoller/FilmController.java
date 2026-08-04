package com.springboot.web.contoller;

import com.springboot.web.common.CommonResult;
import com.springboot.web.common.ServiceException;
import com.springboot.web.common.ServiceExceptionEnum;
import com.springboot.web.common.po.Film;
import com.springboot.web.dto.FilmDto;
import com.springboot.web.vo.FilmVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/film")
@Slf4j
public class FilmController {
    //假装是数据库的数据
    List<Film> list = new ArrayList<>();

    {
        list.add(new Film(1, "学院恐龙", "一位女权主义者和一位疯狂科学家必须在加拿大落基山脉与一位教师战斗的史诗剧",
                2006, 1, null, 6, new BigDecimal("0.99"), 86, new BigDecimal("20.99"),
                "PG", "Deleted Scenes,Behind the Scenes", new Date()));
        list.add(new Film(2, "黄金手指", "一位数据库管理员和一位探险家必须在古代中国找到一辆车的惊人故事",
                2006, 1, null, 3, new BigDecimal("4.99"), 48, new BigDecimal("12.99"),
                "G", "Trailers,Deleted Scenes", new Date()));
        list.add(new Film(3, "改编漏洞", "一位伐木工和一辆汽车必须在气球工厂中击沉一位伐木工的惊人反思",
                2006, 1, null, 7, new BigDecimal("2.99"), 50, new BigDecimal("18.99"),
                "NC-17", "Trailers,Commentaries", new Date()));
    }

    @GetMapping("/{id}")
    public CommonResult<FilmVo> queryById(@PathVariable Integer id) {
        log.info("id = " + id);
        List<Film> list2 = list.stream()
                .filter(f -> f.getFilmId().equals(id))
                .toList();
        if (list2.isEmpty())
            throw new ServiceException(ServiceExceptionEnum.FILM_NOT_EXIST);
        Film film = list2.get(0);
        FilmVo filmVo = new FilmVo();
        BeanUtils.copyProperties(film, filmVo);

        CommonResult<FilmVo> result = new CommonResult<>();
        result.setData(filmVo);
        return result;
    }

    @PostMapping
    public CommonResult<Void> addOne(@RequestBody FilmDto filmDto) {
        log.info("filmDto is  {}", filmDto);
        Film film = new Film();
        BeanUtils.copyProperties(filmDto, film);
        //stream生成个随机整数
        Random random = new Random();
        film.setFilmId(random.nextInt(10000) + 1000);
        if (film.getLastUpdate() == null) {
            film.setLastUpdate(new Date());
        }
        log.info("film is {}", film);
        list.add(film);
        return new CommonResult<>();
    }

    @PutMapping
    public CommonResult<Void> updateOne(@RequestParam(value = "filmid") Integer filmId,
                                        @RequestBody FilmDto filmDto) {
        log.info("id is {}", filmId);
        log.info("filmDto is {}", filmDto);
        boolean exists = list.stream().anyMatch(f -> f.getFilmId().equals(filmId));
        if (!exists) {
            throw new ServiceException(ServiceExceptionEnum.FILM_NOT_EXIST);
        }
        list.stream().filter(f -> f.getFilmId().equals(filmId))
                .findFirst().ifPresent(po -> {
                    po.setFilmId(filmId);
                    po.setTitle(filmDto.getTitle());
                    po.setDescription(filmDto.getDescription());
                    po.setReleaseYear(filmDto.getReleaseYear());
                    po.setLanguageId(filmDto.getLanguageId());
                    po.setOriginalLanguageId(filmDto.getOriginalLanguageId());
                    po.setRentalDuration(filmDto.getRentalDuration());
                    po.setRentalRate(filmDto.getRentalRate());
                    po.setLength(filmDto.getLength());
                    po.setReplacementCost(filmDto.getReplacementCost());
                    po.setRating(filmDto.getRating());
                    po.setSpecialFeatures(filmDto.getSpecialFeatures());
                    po.setLastUpdate(filmDto.getLastUpdate() != null ? filmDto.getLastUpdate() : new Date());
                });
        log.info("list is {}", list);
        return new CommonResult<>();
    }

    @DeleteMapping("/{id}")
    public CommonResult<Void> deleteOne(@PathVariable Integer id) {
        log.info("id is {}", id);
        list.removeIf(f -> f.getFilmId().equals(id));
        log.info("list is {}", list);
        return new CommonResult<>();
    }

    @GetMapping("/queryByCondition")
    public CommonResult<List<FilmVo>> queryByCondition(
            @RequestParam(required = false)
            String title,
            @RequestParam(required = false)
            String rating,
            @RequestParam
            int curPage,
            @RequestParam
            int pageSize) {
        log.info("title is {}", title);
        log.info("rating is {}", rating);
        log.info("curPage is {}", curPage);
        log.info("pageSize is {}", pageSize);
        CommonResult<List<FilmVo>> commonResult = new CommonResult<>();
        List<FilmVo> listVo = list.stream()
                .filter(f -> title == null || title.isEmpty() || (f.getTitle() != null && f.getTitle().contains(title)))
                .filter(f -> rating == null || rating.isEmpty() || rating.equals(f.getRating()))
                .map(po -> {
                    FilmVo vo = new FilmVo();
                    BeanUtils.copyProperties(po, vo);
                    return vo;
                }).toList();
        commonResult.setData(listVo);
        return commonResult;
    }
}
