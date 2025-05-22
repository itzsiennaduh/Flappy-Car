package seng201.team124.factories;

import seng201.team124.models.racelogic.Race;

import java.util.ArrayList;
import java.util.List;

public class RaceFactory {

    public static List<Race> getRaces() {
        List<Race> races = new ArrayList<>();

        races.add(hockenheimring());
        races.add(gnirmiehnekcoh());
        races.add(heimringhocken());

        return races;
    }

    public static Race hockenheimring() {
        return new Race(
                "Hockenheimring",
                5,
                List.of(RouteFactory.roadRoute()),
                6,
                10000,
                "/assets/models/RaceTacks/racetrackplease_v0.3.obj"
                );
    }

    public static Race gnirmiehnekcoh() {
        return new Race(
                "Gnirmiehnekcoh",
                5,
                List.of(RouteFactory.mudRoute()),
                8,
                10000,
                "/assets/models/RaceTacks/racetrackplease1_v0.3.obj"
                );
    }
    public static Race heimringhocken() {
        return new Race(
                "Heimringhocken",
                5,
                List.of(RouteFactory.iceRoute()),
                8,
                10000,
                "/assets/models/RaceTacks/racetrackplease2_v0.3.obj"
                );
    }
}

