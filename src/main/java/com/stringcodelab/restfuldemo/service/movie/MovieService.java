package com.stringcodelab.restfuldemo.service.movie;

import com.stringcodelab.restfuldemo.domain.movie.Movie;
import com.stringcodelab.restfuldemo.repository.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieService.class);

    @Autowired
    private MovieRepository movieRepository;

    public List<Movie> getAllMovies() {
        final List<Movie> allMovies = movieRepository.getAllMovies()
                .stream()
                .sorted(Comparator.comparing(Movie::getTitle))
                .collect(Collectors.toList());

        LOGGER.info("Total Movies " + allMovies.size());
        return allMovies;
    }

    public List<Movie> getMoviesForReleaseYear(int year) {
        List<Movie> allMovies = movieRepository.getAllMovies();
        Predicate<Movie> movieOfYearPredicate = movie -> movie.getReleaseYear() == year;
        List<Movie> sortedListOfMovies = allMovies.stream()
                .filter(movieOfYearPredicate)
                .sorted(Comparator.comparing(Movie::getTitle))
                .collect(
                        Collectors.toList()
                );

        LOGGER.info("Total Movies in year [" + year + "] = " + sortedListOfMovies.size());

        return sortedListOfMovies;
    }

    public List<Movie> getMoviesForStartAndEndYear(int startYear, int endYear) {
        if(startYear > endYear) {
            throw new IllegalArgumentException("Start Year cannot be greater than End Year");
        }

        final List<Movie> allMovies = movieRepository.getAllMovies();
        final Predicate<Movie> movieStartYear = movie -> movie.getReleaseYear() >= startYear;
        final Predicate<Movie> movieEndYear = movie -> movie.getReleaseYear() <= endYear;

        final List<Movie> movies = allMovies.stream()
                .filter(movieStartYear.and(movieEndYear))
                .sorted(Comparator.comparing(Movie::getReleaseYear))
                .collect(Collectors.toList());

        LOGGER.info("Total Movies between " + startYear + " and " + endYear + " are " + movies.size());

        return movies;

    }

    public Integer getYearWithMostMovies() {
        List<Movie> allMovies = movieRepository.getAllMovies();
        final Map.Entry<Integer, Long> yearWithMostMovies = allMovies.stream()
                .collect(Collectors.groupingBy(Movie::getReleaseYear, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .get();

        LOGGER.info("Year with most movies is " + yearWithMostMovies.getKey() + " with total movies " + yearWithMostMovies.getValue());

        return yearWithMostMovies.getKey();
    }

    public List<Movie> getMovieDetailsOfYearWithMostMovies() {
        final List<Movie> allMovies = movieRepository.getAllMovies();

        final Map<Integer, List<Movie>> collect = allMovies.stream().collect(Collectors.groupingBy(Movie::getReleaseYear));

        final Map.Entry<Integer, Long> yearWithMostMovies = allMovies.stream()
                .collect(Collectors.groupingBy(Movie::getReleaseYear, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .get();

        final List<Movie> movies = collect.get(yearWithMostMovies.getKey())
                .stream()
                .sorted(Comparator.comparing(Movie::getTitle))
                .collect(Collectors.toList());

        LOGGER.info("Size of the movies " + movies.size());

        return movies;
    }
}
