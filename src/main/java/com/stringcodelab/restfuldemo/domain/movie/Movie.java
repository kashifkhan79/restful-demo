package com.stringcodelab.restfuldemo.domain.movie;

import com.stringcodelab.restfuldemo.domain.actor.Actor;

import java.util.ArrayList;
import java.util.List;

public class Movie {

    private int releaseYear;
    private String title;
    private List<Actor> actors;

    public Movie() {
    }

    public Movie(int releaseYear, String title, List<Actor> actors) {
        this.releaseYear = releaseYear;
        this.title = title;
        this.actors = actors;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Actor> getActors() {
        if(actors == null) {
            actors = new ArrayList<>();
        }
        return actors;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "releaseYear=" + releaseYear +
                ", title='" + title + '\'' +
                ", actors=" + actors +
                '}';
    }
}
