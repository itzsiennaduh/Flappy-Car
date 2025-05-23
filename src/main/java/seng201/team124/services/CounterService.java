package seng201.team124.services;

/**
 * Simple service class to interact with counters
 * @author seng201 teaching team and team124
 */
public class CounterService {
    private double elapsedSeconds = 0;
    private double totalRaceHours;
    private boolean raceInProgress = false;
    private double raceTimeLimit = -1;
    private double currentRaceTime = 0;

    /**
     * Gets the formatted elapsed time in the format HH:MM:SS (makes time look pretty)
     * @return formatted time
     */
    public String getFormattedElapsedTime() {
        int totalSeconds = (int) elapsedSeconds;
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    /**
     * modifies the time
     * @param seconds hours to add or subtract
     */
    public void modifyTime(double seconds) {
        elapsedSeconds += seconds;
        elapsedSeconds = Math.max(0, elapsedSeconds); //doesnt go negative
    }

    /**
     * sets the race in progress flag
     * @param inProgress true if the race is in progress, false otherwise
     */
    public void setRaceInProgress(boolean inProgress) {
        this.raceInProgress = inProgress;
    }

    /**
     * gets elapsed seconds
     * @return elapsed seconds
     */
    public double getElapsedSeconds() {
        return this.elapsedSeconds;
    }

    /**
     * calculates remaining time
     * @return the remaining time of the race
     */
    public double getRemainingTime() {
        if (raceTimeLimit <= 0) return 0;
        return Math.max(0, raceTimeLimit - currentRaceTime);
    }

    /**
     * is the race still in progress?
     * @return true if it is
     */
    public boolean isRaceInProgress() {
        return raceInProgress;
    }

    /**
     * starts the race, sets the race in progress flag to true and resets the elapsed seconds and race time limit
     */
    public void startRace() {
        raceInProgress = true;
        currentRaceTime = 0;
        elapsedSeconds = 0;
        System.out.println("Starting race. Time limit: " + raceTimeLimit);
    }

    /**
     * stops the race, sets the race in progress flag to false and resets the elapsed seconds and race time limit
     */
    public void stopRace() {
        raceInProgress = false;
        System.out.println("Stopping race");
    }

    /**
     * sets the race time limit to the given limit
     * @param limit the limit to set
     */
    public void setRaceTimeLimit(double limit) {
        this.raceTimeLimit = limit * 3600; //limit in hrs, change to minutes
        this.currentRaceTime = 0; //current race time in seconds
        System.out.println("Setting race time limit to " + limit + " hours");
    }

    /**
     * gets the race time limit
     * @return race time limit
     */
    public double getRaceTimeLimit() {
        System.out.println("Race time limit is " + raceTimeLimit + " hours");
        return raceTimeLimit;
    }

    /**
     * checks if the race has expired, meaning the race time limit has been reached
     * @return true if the race has expired, false otherwise.
     */
    public boolean hasRaceTimeExpired() {
        return raceTimeLimit > 0 && isRaceInProgress() && currentRaceTime >= raceTimeLimit;
    }

    /**
     * increments the current race time by the given delta time.
     * @param deltaTime the amount of time to increment the current race time by.
     */
    public void incrementRaceTime(double deltaTime) {

        currentRaceTime += deltaTime;
        elapsedSeconds += deltaTime;
    }

    /**
     * gets race time limit
     * @return total hours in the race
     */
    public double getTotalRaceHours() {
        GameManager gm = GameManager.getInstance();
        totalRaceHours = gm.getTimeLimit();
        return totalRaceHours;
    }

    /**
     * gets the current race time in seconds
     * @return current race time in seconds.
     */
    public double getCurrentRaceTime() {
        return currentRaceTime;
    }



}
