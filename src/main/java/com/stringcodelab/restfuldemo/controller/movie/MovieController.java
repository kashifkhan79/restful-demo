package com.stringcodelab.restfuldemo.controller.movie;

import com.stringcodelab.restfuldemo.domain.movie.Movie;
import com.stringcodelab.restfuldemo.service.movie.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MovieController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieController.class);

    @Autowired
    private MovieService movieService;

    @RequestMapping(value = "/movies/all", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<Movie> getAllMovies() {
        return movieService.getAllMovies();
    }

    @RequestMapping(value = "/movies", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<Movie> getMoviesForReleaseYear(@RequestParam(value = "year")int year) {
        return movieService.getMoviesForReleaseYear(year);
    }

    @RequestMapping(value = "/movies/between", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<Movie> getMovieForStartAndEndYear(@RequestParam(value = "startYear") int startYear, @RequestParam(value = "endYear") int endYear) {
        return movieService.getMoviesForStartAndEndYear(startYear, endYear);
    }

    @RequestMapping(value = "/movies/year", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Integer getYearWithMostMovies() {
        return movieService.getYearWithMostMovies();
    }

    @RequestMapping(value = "movies/year/details", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<Movie> getMovieDetailsOfYearWithMostMovies() {
        return movieService.getMovieDetailsOfYearWithMostMovies();
    }

}
