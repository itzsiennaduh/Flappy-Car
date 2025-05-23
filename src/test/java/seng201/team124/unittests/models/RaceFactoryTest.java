package seng201.team124.unittests.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng201.team124.factories.RaceFactory;
import seng201.team124.models.raceLogic.Race;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RaceFactoryTest {

    private List<Race> races;

    @BeforeEach
    public void setUp() {
        races = RaceFactory.getRaces();
    }

    @Test
    public void testGetRacesNotNull() {
        assertNotNull(races);
    }

    @Test
    public void testGetRacesReturnsCorrectNumberOfRaces() {
        assertEquals(3, races.size());
    }

    @Test
    public void testHockenheimringRaceDetails() {
        Race hockenheimring = races.stream()
                .filter(race -> "Hockenheimring".equals(race.getName()))
                .findFirst()
                .orElse(null);

        assertNotNull(hockenheimring);
        assertEquals(6, hockenheimring.getEntries());
        assertEquals(10000, hockenheimring.getPrizeMoney());
        assertEquals("/assets/models/RaceTacks/racetrackplease_v0.3.obj", hockenheimring.getRaceURL());
        assertEquals(1, hockenheimring.getNumberOfRoutes());
    }

    @Test
    public void testGnirmiehnekcohRaceDetails() {
        Race gnirmiehnekcoh = races.stream()
                .filter(race -> "Gnirmiehnekcoh".equals(race.getName()))
                .findFirst()
                .orElse(null);

        assertNotNull(gnirmiehnekcoh);
        assertEquals(8, gnirmiehnekcoh.getEntries());
        assertEquals(10000, gnirmiehnekcoh.getPrizeMoney());
        assertEquals("/assets/models/RaceTacks/racetrackplease1_v0.3.obj", gnirmiehnekcoh.getRaceURL());
        assertEquals(1, gnirmiehnekcoh.getNumberOfRoutes());
    }

    @Test
    public void testHeimringhockenRaceDetails() {
        Race heimringhocken = races.stream()
                .filter(race -> "Heimringhocken".equals(race.getName()))
                .findFirst()
                .orElse(null);

        assertNotNull(heimringhocken);
        assertEquals(8, heimringhocken.getEntries());
        assertEquals(10000, heimringhocken.getPrizeMoney());
        assertEquals("/assets/models/RaceTacks/racetrackplease2_v0.3.obj", heimringhocken.getRaceURL());
        assertEquals(1, heimringhocken.getNumberOfRoutes());
    }
}