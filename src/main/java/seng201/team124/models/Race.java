package seng201.team124.models;

import java.util.ArrayList;
import java.util.List;

/**
 * race class with its stats.
 * each race needs a name, a length in hours, a list of routes, a number of entries, and a prize money amount, and at least one route.
 */
public class Race {
    private final String name;
    private final int hours; //in hours
    private final List<Route> routes; //select race route, at least 1
    private final int entries; //number of competitors
    private final int prizeMoney; //amount of prize money for first place

    /**
     * constructor for the race class.
     * creates a new race with the specified characteristics.
     * @param name the name of the race.
     * @param hours duration of race in hours.
     * @param routes list of routes for the race. At least one route.
     * @param entries number of competitors excluding the player.
     * @param prizeMoney amount of prize money for first place.
     */
    public Race(String name, int hours, List<Route> routes, int entries, int prizeMoney) {
        this.name = name;
        this.hours = hours;
        this.routes = new ArrayList<>(routes);
        this.entries = entries;
        this.prizeMoney = prizeMoney;
    }

    //getters
    public String getName() {
        return this.name;
    }

    public int getHours() {
        return this.hours;
    }

    public List<Route> getRoutes() {
        return new ArrayList<>(routes);
    }

    public Route getRoute(int index) {
        return routes.get(index);
    }

    public int getNumberOfRoutes() {
        return routes.size();
    }

    public int getEntries() {
        return this.entries;
    }

    public int getPrizeMoney() {
        return this.prizeMoney;
    }

    //IMPROVE
    public int calculatePrizeMoney(int place) {
        if (place <= 0) {
            return 0; //Invalid or DNF
        }
        return (int) (place * prizeMoney);
    }

    public String toString() {
        return  "Race: " + getName() +
                "\nDuration: " + getHours() + " hours" +
                "\nThis race has: " + getNumberOfRoutes() + " number of routes." +
                "\nRoutes: " + getRoutes() +
                "\nCompetitors: " + getEntries() +
                "\nFirst Place Prize: $" + getPrizeMoney();
    }
}
