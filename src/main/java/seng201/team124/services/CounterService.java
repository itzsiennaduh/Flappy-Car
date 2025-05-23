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

    public void startRace() {
        raceInProgress = true;
        currentRaceTime = 0;
        elapsedSeconds = 0;
        System.out.println("Starting race. Time limit: " + raceTimeLimit);
    }

    public void stopRace() {
        raceInProgress = false;
        System.out.println("Stopping race");
    }

    public void setRaceTimeLimit(double limit) {
        this.raceTimeLimit = limit * 3600; //limit in hrs, change to minutes
        this.currentRaceTime = 0; //current race time in seconds
        System.out.println("Setting race time limit to " + limit + " hours");
    }

    public double getRaceTimeLimit() {
        System.out.println("Race time limit is " + raceTimeLimit + " hours");
        return raceTimeLimit;
    }

    public boolean hasRaceTimeExpired() {
        return raceTimeLimit > 0 && isRaceInProgress() && currentRaceTime >= raceTimeLimit;
    }

    public void incrementRaceTime(double deltaTime) {

        currentRaceTime += deltaTime;
        elapsedSeconds += deltaTime;
//        System.out.println("Time: " + elapsedSeconds + " seconds. Current time: " + currentRaceTime);

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

    public double getCurrentRaceTime() {
        return currentRaceTime;
    }



}
