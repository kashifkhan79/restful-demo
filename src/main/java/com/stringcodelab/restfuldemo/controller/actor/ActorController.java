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

    @RequestMapping(value = "/actor/active", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Actor getActorWithMostPlayedMovies() {
        return actorService.getActorWithMostPlayedMovies();
    }

    @RequestMapping(value = "/actor/movies", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<Movie> getMoviesForActor(@RequestParam(value = "firstName") String firstName, @RequestParam(value = "lastName") String lastName) {
        return actorService.getMoviesForActor(firstName, lastName);
    }

}
