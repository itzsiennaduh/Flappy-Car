package seng201.team124.services;

import seng201.team124.models.*;

/**
 * Simple service class to interact with counters
 * @author seng201 teaching team and team124
 */
public class CounterService {
    private double elapsedHours;
    private double totalRaceHours;
    private boolean isRaceInProgress;

    public void startRace(Race race) {
        this.elapsedHours = 0;
        this.totalRaceHours = race.getHours();
        this.isRaceInProgress = true;
    }

    /**
     * increases race time
     *
     * @param deltaHours time increasing in hours
     * @return true if race is in progress
     */
    public boolean incrementRaceTime(double deltaHours) {
        if (!isRaceInProgress) return false;
        elapsedHours += deltaHours;
        if (elapsedHours >= totalRaceHours) {
            isRaceInProgress = false;
        }
        return isRaceInProgress;
    }

    /**
     * modifies the time
     * @param hours hours to add or subtract
     */
    public void modifyTime(double hours) {
        elapsedHours += hours;
        elapsedHours = Math.max(0, elapsedHours); //doesnt go negative
    }

    /**
     * make time look pretty :)
     *
     * @return pretty time
     */
    public String getFormattedTime() {
        int hours = (int) elapsedHours;
        int minutes = (int) ((elapsedHours - hours) * 60);
        return String.format("%d:%02d", hours, minutes);
    }

    /**
     * gets elapsed hours
     * @return elapsed hours
     */
    public double getElapsedHours() {
        return this.elapsedHours;
    }

    /**
     * calculates remaining hours
     * @return the remaining hours of the race
     */
    public double getRemainingHours() {
        return this.totalRaceHours - this.elapsedHours;
    }

    /**
     * is the race still in progress?
     * @return true if it is
     */
    public boolean isRaceInProgress() {
        return this.isRaceInProgress;
    }

}
