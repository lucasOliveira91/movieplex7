package org.javaee7.movieplex7.booking;

import lombok.Data;
import org.javaee7.movieplex7.entities.Movie;
import org.javaee7.movieplex7.entities.ShowTiming;

import javax.faces.flow.FlowScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.StringTokenizer;

@Named
@FlowScoped("booking")
@Data
public class Booking {

    int movieId;
    String startTime;
    int startTimeId;

    @PersistenceContext
    EntityManager em;

    public String getMovieName() {
        try {
            return em.createNamedQuery("Movie.findById", Movie.class)
                    .setParameter("id", movieId)
                    .getSingleResult()
                    .getName();
        } catch (NoResultException e) {
            return "";
        }
    }

    public String getStartTime() {
        return startTime;
    }
    public void setStartTime(String startTime) {
        StringTokenizer tokens = new StringTokenizer(startTime, ",");
        startTimeId = Integer.parseInt(tokens.nextToken());
        this.startTime = tokens.nextToken();
    }
    public int getStartTimeId() {
        return startTimeId;
    }

    public String getTheater() {
// for a movie and show
        try {
// Always return the first theater
            List<ShowTiming> list =
                    em.createNamedQuery("ShowTiming.findByMovieAndTimingId",
                            ShowTiming.class)
                            .setParameter("movieId", movieId)
                            .setParameter("timingId", startTimeId)
                            .getResultList();
            if (list.isEmpty())
                return "none";
            return list
                    .get(0)
                    .getTheaterId()
                    .getId().toString();
        } catch (NoResultException e) {
            return "none";
        }
    }
}
