package seng201.team124.models.raceLogic;

/**
 * Simple counter-class to keep track of the current time
 * @author seng201 teaching team and team124
 */
public class Counter {
    private double currentTime;

    /**
     * Constructor
     */
    public Counter(double initialTime) {
        this.currentTime = initialTime;
    }

    /**
     * add hours to the count
     */
    public void addHours(double hours) {
        this.currentTime += hours;
    }

    /**
     * subtract hours from the count
     */
    public void subtractHours(double hours) {
        this.currentTime -= hours;
    }

    /**
     * make time look pretty :)
     *
     * @return pretty time
     */
    public String getFormattedTime() {
        int hours = (int) currentTime;
        int minutes = (int) ((currentTime - hours) * 60);
        return String.format("%d:%02d", hours, minutes);
    }

    /**
     * Get current time
     * @return Current time in hrs
     */
    public double getCurrentTime() {
        return this.currentTime;
    }
}
