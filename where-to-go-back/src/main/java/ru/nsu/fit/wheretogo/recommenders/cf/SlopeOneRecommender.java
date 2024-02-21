package ru.nsu.fit.wheretogo.recommenders.cf;

import ru.nsu.fit.wheretogo.entity.place.Place;
import ru.nsu.fit.wheretogo.entity.user.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class SlopeOneRecommender {
    private static final int TOP_RECOMMENDATIONS = 5;

    private SlopeOneRecommender() {
    }

    private static final Map<Place, Map<Place, Double>> diff = new HashMap<>();
    private static final Map<Place, Map<Place, Integer>> freq = new HashMap<>();

    public static Map<User, HashMap<Place, Double>> slopeOne(Map<User, HashMap<Place, Double>> data, List<Place> placeList) {
        buildDifferencesMatrix(data);
        return predict(data, placeList);
    }

    private static void buildDifferencesMatrix(Map<User, HashMap<Place, Double>> data) {
        for (HashMap<Place, Double> user : data.values()) {
            for (Map.Entry<Place, Double> placeEntry : user.entrySet()) {
                if (!diff.containsKey(placeEntry.getKey())) {
                    diff.put(placeEntry.getKey(), new HashMap<>());
                    freq.put(placeEntry.getKey(), new HashMap<>());
                }

                for (Map.Entry<Place, Double> placeEntryInner : user.entrySet()) {
                    var oldCount = 0;

                    if (freq.get(placeEntry.getKey()).containsKey(placeEntryInner.getKey())) {
                        oldCount = freq.get(placeEntry.getKey()).get(placeEntryInner.getKey());
                    }

                    var oldDiff = 0.0;

                    if (diff.get(placeEntry.getKey()).containsKey(placeEntryInner.getKey())) {
                        oldDiff = diff.get(placeEntry.getKey()).get(placeEntryInner.getKey());
                    }

                    double observedDiff = placeEntry.getValue() - placeEntryInner.getValue();

                    freq.get(placeEntry.getKey()).put(placeEntryInner.getKey(), oldCount + 1);
                    diff.get(placeEntry.getKey()).put(placeEntryInner.getKey(), oldDiff + observedDiff);
                }
            }
        }

        for (Place j : diff.keySet()) {
            for (Place i : diff.get(j).keySet()) {
                double oldValue = diff.get(j).get(i);
                int count = freq.get(j).get(i);
                diff.get(j).put(i, oldValue / count);
            }
        }

    }

    private static Map<User, HashMap<Place, Double>> predict(Map<User, HashMap<Place, Double>> data, List<Place> places) {
        Map<User, HashMap<Place, Double>> outputData = new HashMap<>();
        HashMap<Place, Double> uPred = new HashMap<>();
        HashMap<Place, Integer> uFreq = new HashMap<>();

        for (Place placeDiff : diff.keySet()) {
            uFreq.put(placeDiff, 0);
            uPred.put(placeDiff, 0.0);
        }

        for (Map.Entry<User, HashMap<Place, Double>> userMapEntry : data.entrySet()) {
            for (Place place : userMapEntry.getValue().keySet()) {
                for (Place placeDiff : diff.keySet()) {
                    if (places.contains(placeDiff)) {
                        try {
                            double predictedValue = diff.get(placeDiff).get(place) + userMapEntry.getValue().get(place);
                            double finalValue = predictedValue * freq.get(placeDiff).get(place);
                            uPred.put(placeDiff, uPred.get(placeDiff) + finalValue);
                            uFreq.put(placeDiff, uFreq.get(placeDiff) + freq.get(placeDiff).get(place));
                        } catch (NullPointerException ignored) {

                        }
                    }
                }
            }

            HashMap<Place, Double> clean = new HashMap<>();

            for (Place place : uPred.keySet()) {
                if (uFreq.get(place) > 0) {
                    clean.put(place, uPred.get(place) / uFreq.get(place));
                }
            }

            for (Place place : places) {
                if (userMapEntry.getValue().containsKey(place)) {
                    clean.put(place, userMapEntry.getValue().get(place));
                } else if (!clean.containsKey(place)) {
                    clean.put(place, -1.0);
                }
                if (clean.size() >= TOP_RECOMMENDATIONS) {
                    break;
                }
            }

            outputData.put(userMapEntry.getKey(), clean);
        }

        return outputData;
    }
}
