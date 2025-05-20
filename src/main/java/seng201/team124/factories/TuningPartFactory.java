package seng201.team124.factories;

import seng201.team124.models.vehicleutility.TuningParts;

/**
 * stores each tuning part available in the game.
 */
public class TuningPartFactory {

    //methods to create different tuning parts
    public static TuningParts createNitrousOxide() {
        return new TuningParts(
                "Nitrous Oxide", //name
                "Enhances speed, diminishes fuel economy", //description
                5,   //speed
                0,   //handling
                0,   //reliability
                -2,  //fuel eco
                500  //cost
        );
    }

    public static TuningParts createTurbocharger() {
        return new TuningParts(
                "Turbocharger", //name
                "Enhances speed, diminishes reliability", //description
                5,   //speed
                0,   //handling
                -3,  //reliability
                0,   //fuel eco
                1500 //cost
        );
    }

    public static TuningParts createSupercharger() {
        return new TuningParts(
                "Supercharger", //name
                "Enhances speed, diminishes reliability and fuel economy. Cheaper than Turbocharger.", //description
                5,   //speed
                0,   //handling
                -2,  //reliability
                -3,  //fuel eco
                1000 //cost
        );
    }

    public static TuningParts createOffRoadTires() {
        return new TuningParts(
                "Off Road Tires", //name
                "Increases handling, great for races with handling debuffs.", //description
                0,   //speed
                4,   //handling
                0,   //reliability
                0,   //fuel eco
                1000 //cost
        );
    }
}
