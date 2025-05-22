package seng201.team124.models.racelogic;

import seng201.team124.factories.RaceFactory;
import seng201.team124.factories.RouteFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * race class with its stats.
 * each race needs a name, a length in hours, a list of routes, a number of entries, and a prize money amount, and at least one route.
 */
public class Race {
    private final String name;
    private final double hours; //in hours
    private final List<Route> routes; //select race route, at least 1
    private final int entries; //number of competitors
    private final double prizeMoney; //amount of prize money for first place
    private final String raceURL;

    /**
     * constructor for the race class.
     * creates a new race with the specified characteristics.
     * @param name the name of the race.
     * @param hours duration of race in hours.
     * @param routes list of routes for the race. At least one route.
     * @param entries number of competitors excluding the player.
     * @param prizeMoney amount of prize money for first place.
     */
    public Race(String name, double hours, List<Route> routes, int entries, double prizeMoney, String raceURL) {
        this.name = name;
        this.hours = hours;
        this.routes = new ArrayList<>(routes);
        this.entries = entries;
        this.prizeMoney = prizeMoney;
        this.raceURL = raceURL;
    }

    //getters
    /**
     * gets the name of the race.
     * @return name of the race.
     */
    public String getName() {
        return this.name;
    }

    /**
     * gets the duration of the race in hours.
     * @return duration of the race in hours.
     */
    public double getHours() {
        return this.hours;
    }

    /**
     * gets a list of all routes in the race.
     * @return list of all routes in the race.
     */
    public List<Route> getRoutes() {
        return new ArrayList<>(routes); //copy
    }

    /**
     * gets a specific route from the race.
     *
     * @return the route at the specified index.
     */
    public Route getRoute(){
        return routes.get(0);
    }

    public List<Route> getRoutes(int index) {
        return List.copyOf(routes);
    }

    public Route getRoute(int index) {
        return routes.get(index);
    }

    /**
     * gets the number of routes in the race.
     * @return number of routes in the race, in integer value.
     */
    public int getNumberOfRoutes() {
        return routes.size();
    }

    /**
     * gets the number of competitors excluding the player.
     * @return number of competitors excluding the player, in integer value.
     */
    public int getEntries() {
        return this.entries;
    }

    /**
     * gets the prize money for the first place. other placings prizes are calculated in RaceService.
     * @return prize money for the first place, in dollars (integer value)
     */
    public double getPrizeMoney() {
        return this.prizeMoney;
    }

    public String getRaceURL() {return this.raceURL;}

    /**
     * calculates the time penalty for a specific number of hours.
     * @param penalty the number of hours to apply the penalty to.
     * @return new time
     */
    public double applyTimePenalty(double penalty) {
        return this.hours - penalty;
    }

    /**
     * @return a string with the race's name, duration, number of routes, routes, competitors, and prize money.
     * as per the design brief/specs.
     */
    @Override
    public String toString() {
        return  "Race: " + getName() +
                "\nDuration: " + getHours() + " hours" +
                "\nThis race has: " + getNumberOfRoutes() + " number of routes." +
                "\nRoutes: " + getRoutes() +
                "\nCompetitors: " + getEntries() +
                "\nFirst Place Prize: $" + getPrizeMoney();
    }
}
