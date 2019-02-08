package com.stringcodelab.restfuldemo.repository;

import com.stringcodelab.restfuldemo.domain.movie.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MovieRepository {

    @Autowired
    private List<Movie> movies;

    public List<Movie> getAllMovies() {
        return movies;
    }
}
