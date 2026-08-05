package com.springboot.sakila.mapper;

import com.springboot.sakila.po.Film;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FilmMapper {

    @Insert("insert into film(title, description, release_year, language_id, original_language_id, rental_duration, rental_rate, length, replacement_cost, rating, special_features) " +
            "values(#{title}, #{description}, #{releaseYear}, #{languageId}, #{originalLanguageId}, #{rentalDuration}, #{rentalRate}, #{length}, #{replacementCost}, #{rating}, #{specialFeatures})")
    void insertOne(Film film);

    @Update("update film set title=#{title}, description=#{description}, release_year=#{releaseYear}, language_id=#{languageId}, original_language_id=#{originalLanguageId}, rental_duration=#{rentalDuration}, rental_rate=#{rentalRate}, length=#{length}, replacement_cost=#{replacementCost}, rating=#{rating}, special_features=#{specialFeatures} where film_id=#{filmId}")
    void updateOne(Film film);

    @Delete("delete from film where film_id=#{id}")
    void deleteOne(Long id);

    @Select("select * from film where film_id = #{id}")
    Film selectOne(Long id);

    @Select("select * from film")
    List<Film> selectAll();
}
