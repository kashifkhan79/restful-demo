package com.stringcodelab.restfuldemo.controller.actor;

import com.stringcodelab.restfuldemo.domain.actor.Actor;
import com.stringcodelab.restfuldemo.domain.movie.Movie;
import com.stringcodelab.restfuldemo.service.actor.ActorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ActorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActorController.class);

    @Autowired
    private ActorService actorService;

    @RequestMapping(value = "/actors/all", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<Actor> getAllActors() {
        return actorService.getAllActors();
    }

    @RequestMapping(value = "/actor/movies", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<Movie> getMoviesForActor(@RequestParam(value = "firstName") String firstName, @RequestParam(value = "lastName") String lastName) {
        return actorService.getMoviesForActor(firstName, lastName);
    }

    @RequestMapping(value = "/actor/movies/min", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Actor getActorWithLeastPlayedMovies() {
        return actorService.getActorWithMinimumPlayedMovies();
    }

    @RequestMapping(value = "/actor/movies/max", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Actor getActorWithMostPlayedMovies() {
        return actorService.getActorWithMostPlayedMovies();
    }

    @RequestMapping(value = "/actor/movies/min/year", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Actor getActorWithLeastPlayedMoviesInGivenYear(@RequestParam(value = "value") int value) {
        return actorService.getActorWithLeastMoviesInGivenYear(value);
    }

    @RequestMapping(value = "/actor/movies/max/year", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Actor getActorWithMostPlayedMoviesInGivenYear(@RequestParam(value = "value") int value) {
        return actorService.getActorWithMaxMoviesInGivenYear(value);
    }

    @RequestMapping(value = "/actor/active/year", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Actor getActorWithMostPlayedMoviesInAnyYear() {
        return actorService.getActorWithMostMoviesInAnySingleYear();
    }
}
