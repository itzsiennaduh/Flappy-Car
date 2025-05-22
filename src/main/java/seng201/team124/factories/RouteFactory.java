package seng201.team124.factories;

import seng201.team124.models.raceLogic.Route;

import java.util.ArrayList;
import java.util.List;

public class RouteFactory {
    public static List<Route> getRoute() {
        List<Route> routes = new ArrayList<>();

        routes.add(roadRoute());
        routes.add(mudRoute());
        routes.add(iceRoute());

        return routes;
    }

    public static Route roadRoute() {
        return new Route(
                "This is a flat road route",
                8,
                2,
                1,
                1,
                1,
                1,
                1
        );
    }

    public static Route mudRoute() {
        return new Route(
                "This is a flat off road mud route",
                8,
                2,
                3,
                0.75,
                0.5,
                0.25,
                0.25
        );
    }

    public static Route iceRoute(){
        return new Route(
                "This is a flat off road ice route",
                8,
                2,
                5,
                3,
                0.1,
                0.25,
                1
        );
    }

}
