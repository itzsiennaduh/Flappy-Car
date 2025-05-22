package seng201.team124.services;

import seng201.team124.models.racelogic.Race;

/**
 * Simple service class to interact with counters
 * @author seng201 teaching team and team124
 */
public class CounterService {
    private double elapsedSeconds = 0;
    private double totalRaceHours;
    private boolean raceInProgress = true;

    /**
     * increases race time
     * @param deltaSeconds time increasing in seconds
     */
    public void incrementRaceTime(double deltaSeconds) {
        elapsedSeconds += deltaSeconds;
    }

    public String getFormattedElapsedTime() {
        int totalSeconds = (int) elapsedSeconds;
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    /**
     * modifies the time
     * @param hours hours to add or subtract
     */
    public void modifyTime(double hours) {
        elapsedSeconds += hours;
        elapsedSeconds = Math.max(0, elapsedSeconds); //doesnt go negative
    }

    /**
     * make time look pretty :)
     *
     * @return pretty time
     */
    public static String getFormattedTime(long millis) {
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        return String.format("%02d:%02d:%02d", hours % 24, minutes % 60, seconds % 60);
    }

    public void setRaceInProgress(boolean inProgress) {
        this.raceInProgress = inProgress;
    }

    /**
     * gets elapsed hours
     * @return elapsed hours
     */
    public double getElapsedHours() {
        return this.elapsedSeconds;
    }

    /**
     * calculates remaining hours
     * @return the remaining hours of the race
     */
    public double getRemainingHours() {
        return this.totalRaceHours - this.elapsedSeconds;
    }

    /**
     * is the race still in progress?
     * @return true if it is
     */
    public boolean isRaceInProgress() {
        return raceInProgress;
    }

    public void stopRace() {
        raceInProgress = false;
    }

}
