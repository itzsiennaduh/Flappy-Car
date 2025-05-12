package seng201.team124.models;

/**
 * stores each vehicle available in the game.
 * the stats excluding cost must total 20 (spread stats out).
 */
public class VehicleFactory {

    //methods to create different vehicles
    public static Vehicle createRedVehicle() {
        return new Vehicle(
                "Ferrari", //name EXAMPLE
                10,  //speed
                5,   //handling
                2,   //reliability
                3,   //fuel eco
                9000 //cost
        );
    }

    public static Vehicle createCatVehicle() {
        return new Vehicle(
                "Kittyyyyy", //name EXAMPLE
                5,  //speed
                5,   //handling
                5,   //reliability
                5,   //fuel eco
                5000 //cost
        );
    }

    public static Vehicle createHorseChariot() {
        return new Vehicle(
                "Horse Chariot", //name EXAMPLE
                9,  //speed
                2,   //handling
                4,   //reliability
                5,   //fuel eco
                10000 //cost
        );
    }
}
