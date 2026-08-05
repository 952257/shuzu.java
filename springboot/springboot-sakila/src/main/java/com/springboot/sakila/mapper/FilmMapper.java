package com.springboot.sakila.mapper;

import com.springboot.sakila.po.Film;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FilmMapper {

    @Insert("insert into film(title, description, release_year, language_id, original_language_id, rental_duration, rental_rate, length, replacement_cost, rating, special_features) " +
            "values(#{title}, #{description}, #{releaseYear}, #{languageId}, #{originalLanguageId}, #{rentalDuration}, #{rentalRate}, #{length}, #{replacementCost}, #{rating}, #{specialFeatures})")
    void insertOne(Film film);
}
