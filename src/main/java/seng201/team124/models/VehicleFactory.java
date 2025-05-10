package seng201.team124.models;

/**
 * vehicle Factory stores each vehicle available in the game.
 * the stats excluding cost must total 20 (spread stats out)
 */
public class VehicleFactory {

    //Methods to create different vehicles
    public static Vehicle createRedvehicle() {
        return new Vehicle(
                "Ferrari vehicle", //name EXAMPLE
                10,  //speed
                5,   //handling
                2,   //reliability
                3,   //fuel eco
                9000 //cost
        );
    }
}
