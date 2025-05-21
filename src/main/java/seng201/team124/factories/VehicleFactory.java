package seng201.team124.factories;

import seng201.team124.models.vehicleutility.Vehicle;

import java.util.ArrayList;
import java.util.List;

/**
 * stores each vehicle available in the game.
 * the stats excluding cost must total 20 (spread stats out).
 */
public class VehicleFactory {

    public static List<Vehicle> getAllVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();

        vehicles.add(createRedVehicle());
        vehicles.add(createCatVehicle());
        vehicles.add(createBlueVehicle());

        return vehicles;
    }

    //methods to create different vehicles
    public static Vehicle createRedVehicle() {
        return new Vehicle(
                "Red Supra", //name EXAMPLE
                "Fast but unreliable, will you risk it?", //description
                10,  //speed
                5,   //handling
                2,   //reliability
                3,   //fuel eco
                9000,//cost
                100, //current fuel
                100, //max fuel
                "/assets/models/Cars/Supra.obj"
        );
    }

    public static Vehicle createCatVehicle() {
        return new Vehicle(
                "Kittyyyyy", //name EXAMPLE
                "All rounder, but not the fastest.", //description
                5,  //speed
                5,   //handling
                5,   //reliability
                5,   //fuel eco
                10, //cost
                100,
                100,
                "/assets/models/Cars/Supra.obj"
        );
    }

    public static Vehicle createBlueVehicle() {
        return new Vehicle(
                "Blue Supra", //name EXAMPLE
                "Slower, but more reliable.", //description
                5,  //speed
                2,   //handling
                8,   //reliability
                5,   //fuel eco
                8000, //cost
                100, //current fuel
                100, //max fuel
                "/assets/models/Cars/Supra.obj"
        );
    }
}
