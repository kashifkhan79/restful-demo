package com.stringcodelab.restfuldemo.service.actor;

import com.stringcodelab.restfuldemo.domain.actor.Actor;
import com.stringcodelab.restfuldemo.domain.movie.Movie;
import com.stringcodelab.restfuldemo.repository.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ActorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActorService.class);

    @Autowired
    private MovieRepository movieRepository;

    public List<Actor> getAllActors() {
        final Set<Actor> actors = movieRepository.getAllMovies().stream().flatMap(movie -> movie.getActors().stream()).sorted(Comparator.comparing(Actor::getFullName)).collect(Collectors.toCollection(LinkedHashSet::new));

        LOGGER.info("Total actors " + actors.size());

        return new ArrayList<>(actors);
    }

    public List<Movie> getMoviesForActor(String firstName, String lastName) {

        final List<Movie> allMovies = movieRepository.getAllMovies();
        final List<Movie> moviesByActor = allMovies.stream()
                .filter(
                        movie -> movie.getActors()
                                .stream()
                                .anyMatch(actor -> firstName.equalsIgnoreCase(actor.getFirstName()) && lastName.equalsIgnoreCase(actor.getLastName()))
                ).collect(Collectors.toList());

        LOGGER.info(String.format("%s %s played in %d movies", firstName, lastName, moviesByActor.size()));

        return moviesByActor;
    }


    public Actor getActorWithMostPlayedMovies() {

        final Map.Entry<Actor, Long> actorWithMostMoviesPlayed = movieRepository.getAllMovies()
                .stream()
                .flatMap(movie -> movie.getActors().stream())
                .collect(Collectors.toMap(Function.identity(), actor -> 1L, (c1, c2) -> c1 + c2))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .get();

        LOGGER.info(String.format("Actor %s played in total %d movies ", actorWithMostMoviesPlayed.getKey().getFullName(), actorWithMostMoviesPlayed.getValue() ));

        return actorWithMostMoviesPlayed.getKey();

    }
}
