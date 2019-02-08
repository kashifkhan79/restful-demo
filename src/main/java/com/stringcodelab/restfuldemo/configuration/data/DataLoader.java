package com.stringcodelab.restfuldemo.configuration.data;

import com.stringcodelab.restfuldemo.domain.actor.Actor;
import com.stringcodelab.restfuldemo.domain.movie.Movie;
import com.stringcodelab.restfuldemo.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class DataLoader {

    @Value("${movies.file.path}")
    private String resourceFile;

    @Bean
    public MovieRepository getMovieRepository() {
        return new MovieRepository();
    }

    @Bean
    public List<Movie> getMovies() throws IOException  {
        final List<Movie> movies = new ArrayList<>();
        final InputStream inputStream = new ClassPathResource(resourceFile).getInputStream();

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while((line = reader.readLine()) != null ) {
                movies.add(getMovieWithActors(line));
            }
        }
        return movies;
    }

    private Movie getMovieWithActors(String dataLine) {
        final int startIndexYear = dataLine.indexOf('(') + 1;
        final int endIndexYear = Math.min(dataLine.indexOf(')', startIndexYear), startIndexYear + 4);
        final int startIndexActors = dataLine.indexOf('/');
        final String title = dataLine.substring(0, startIndexYear - 1).trim();
        final int releaseYear = Integer.parseInt(dataLine.substring(startIndexYear, endIndexYear));
        final List<Actor> actors = new ArrayList<>();
        for (final String actorName : dataLine.substring(startIndexActors + 1).split("/")) {
            final int commaIndex = actorName.indexOf(',');
            final String firstName = (commaIndex != -1) ? actorName.substring(commaIndex + 1).trim() : actorName;
            final String lastName = (commaIndex != -1) ? actorName.substring(0, commaIndex).trim() : "";
            actors.add(new Actor(firstName, lastName));
        }
        return new Movie(releaseYear, title, actors);
    }

}
