package ru.nsu.fit.wheretogo.util.helper;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import ru.nsu.fit.wheretogo.model.recommender.RouteRecommenderResponse;

public class RouteCallsHandler {

    private RouteCallsHandler() {
    }

    public static List<LatLng> decodePolyline(RouteRecommenderResponse response) {
        List<List<String>> geometry = response.getDirection().getFeatures().get(0).getGeometry().getCoordinates();
        List<LatLng> polyLineCoordinates = new ArrayList<>();

        for (List<String> latLngCoordinate : geometry) {
            var latLng = new LatLng(
                    (Double.parseDouble(latLngCoordinate.get(1))),
                    (Double.parseDouble(latLngCoordinate.get(0))));

            polyLineCoordinates.add(latLng);
        }

        return polyLineCoordinates;
    }
}
