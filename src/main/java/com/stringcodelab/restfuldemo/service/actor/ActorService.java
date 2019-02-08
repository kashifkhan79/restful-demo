package com.stringcodelab.restfuldemo.service.actor;

import com.stringcodelab.restfuldemo.domain.actor.Actor;
import com.stringcodelab.restfuldemo.domain.movie.Movie;
import com.stringcodelab.restfuldemo.repository.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
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

    public Actor getActorWithMinimumPlayedMovies() {
        final Map.Entry<Actor, Long> actorWithMinimumMoviesPlayed = movieRepository.getAllMovies()
                .stream()
                .flatMap(movie -> movie.getActors().stream())
                .collect(Collectors.toMap(Function.identity(), actor -> 1L, (c1, c2) -> c1 + c2))
                .entrySet()
                .stream()
                .min(Map.Entry.comparingByValue())
                .get();

        LOGGER.info(String.format("Actor %s played in the least amount of %d movies", actorWithMinimumMoviesPlayed.getKey().getFullName(), actorWithMinimumMoviesPlayed.getValue()));

        return actorWithMinimumMoviesPlayed.getKey();
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

    public Actor getActorWithLeastMoviesInGivenYear(int year) {
        final Optional<Map.Entry<Actor, Long>> optionalEntry = movieRepository.getAllMovies()
                .stream()
                .filter(movie -> movie.getReleaseYear() == year)
                .flatMap(movie -> movie.getActors().stream())
                .collect(Collectors.toMap(Function.identity(), a -> 1L, (c1, c2) -> c1 + c2))
                .entrySet()
                .stream()
                .min(Map.Entry.comparingByValue());

        Actor actor = null;
        if(optionalEntry.isPresent()) {
            actor = optionalEntry.get().getKey();

            LOGGER.info(String.format("Actor %s played in %d movies in the year %d", actor.getFullName(), optionalEntry.get().getValue(), year));

        }
        return actor;
    }

    public Actor getActorWithMaxMoviesInGivenYear(int year) {
        final Optional<Map.Entry<Actor, Long>> optionalEntry = movieRepository.getAllMovies()
                .stream()
                .filter(movie -> movie.getReleaseYear() == year)
                .flatMap(movie -> movie.getActors().stream())
                .collect(Collectors.toMap(Function.identity(), a -> 1L, (c1, c2) -> c1 + c2))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue());

        Actor actor = null;
        if(optionalEntry.isPresent()) {
            actor = optionalEntry.get().getKey();

            LOGGER.info(String.format("Actor %s played in %d movies in the year %d", actor.getFullName(), optionalEntry.get().getValue(), year));

        }
        return actor;
    }

    public Actor getActorWithMostMoviesInAnySingleYear() {

        // Java 8 style.
        Supplier<Map<Actor, AtomicLong>> supplier = () -> new HashMap<>();

        BiConsumer<Map<Actor, AtomicLong>, Movie> accumulator = (map, movie) ->
                movie.getActors().forEach(
                        actor ->
                                map.computeIfAbsent(
                                        actor, a-> new AtomicLong()
                                ).incrementAndGet()
                );

        BinaryOperator<Map<Actor, AtomicLong>> combiner = (map1, map2) -> {
            map2.entrySet().forEach(
                    entry1 -> map1.merge(
                            entry1.getKey(),
                            entry1.getValue(),
                            (al1, al2) -> new AtomicLong(al1.get() + al2.get())
                    )
            );
            return map1;
        };

        Map.Entry<Integer, Map.Entry<Actor, AtomicLong>> mostProductiveActorInAnyYear = movieRepository.getAllMovies()
                .stream()
                .collect(
                        Collectors.groupingBy(
                                Movie::getReleaseYear,
                                // Hand crafted collector
                                Collector.of(supplier, accumulator, combiner, Collector.Characteristics.CONCURRENT)
                        )
                )
                .entrySet()
                .stream()
                .collect(Collectors.toMap(entry3 -> entry3.getKey(),
                        entry3 -> entry3.getValue().entrySet().stream().max(Comparator.comparing(entry4 -> entry4.getValue().get()))
                                .get()
                        )
                ).entrySet()
                .stream()
                .max(Comparator.comparing(entry4 -> entry4.getValue().getValue().get()))
                .get();

        final Actor actor = mostProductiveActorInAnyYear.getValue().getKey();
        final int year = mostProductiveActorInAnyYear.getKey();
        final long moviesPlayed = mostProductiveActorInAnyYear.getValue().getValue().get();

        LOGGER.info(String.format("Actor %s appeared in %d movies in %d year", actor.getFullName(), moviesPlayed, year));

        return actor;
    }

}
