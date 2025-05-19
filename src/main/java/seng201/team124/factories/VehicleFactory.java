package seng201.team124.factories;

import seng201.team124.models.Vehicle;

/**
 * stores each vehicle available in the game.
 * the stats excluding cost must total 20 (spread stats out).
 */
public class VehicleFactory {

    //methods to create different vehicles
    public static Vehicle createRedVehicle() {
        return new Vehicle(
                "Ferrari", //name EXAMPLE
                "Fast but unreliable, will you risk it?", //description
                10,  //speed
                5,   //handling
                2,   //reliability
                3,   //fuel eco
                9000, //cost
                100, //current fuel
                100 //max fuel
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
                5000, //cost
                100,
                100
        );
    }

    public static Vehicle createHorseChariot() {
        return new Vehicle(
                "Horse Chariot", //name EXAMPLE
                "Why does this exist", //description
                9,  //speed
                2,   //handling
                4,   //reliability
                5,   //fuel eco
                10000, //cost
                100, //current fuel
                100 //max fuel
        );
    }
}
