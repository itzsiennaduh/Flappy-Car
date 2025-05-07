package seng201.team124.models;

/**
 * Chariot Factory stores each chariot available in the game.
 * the stats excluding cost must total 20 (spread stats out)
 */
public class ChariotFactory {

    //Methods to create different chariots
    @org.jetbrains.annotations.NotNull
    public static Chariot createRedChariot() {
        return new Chariot(
                "Ferrari Chariot", //name EXAMPLE
                10,  //speed
                5,   //handling
                2,   //reliability
                3,   //fuel eco
                9000 //cost
        );
    }
}
