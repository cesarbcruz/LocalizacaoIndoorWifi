/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cesar.com.localizacaoindoorapi.core;

import cesar.com.localizacaoindoorapi.model.Location;
import cesar.com.localizacaoindoorapi.model.Wifi;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author cesar
 */
public class Finder {

    public Location meet(List<Location> persistentLocations, List<Wifi> currentReferences) {
        Map<Location, Double> locationsfound = new HashMap<Location, Double>();
        for (Wifi currentReference : currentReferences) {
            for (Location location : persistentLocations) {
                for (Wifi wifi : location.getReferencesAp()) {
                    if (currentReference.getBssid().equals(wifi.getBssid())) {
                        double diff = Math.abs(currentReference.calculateDistance() - wifi.calculateDistance());
                        if (locationsfound.containsKey(location)) {
                            locationsfound.put(location, diff + locationsfound.get(location));
                        } else {
                            locationsfound.put(location, diff);
                        }
                    }
                }
            }
        }
        if (locationsfound.isEmpty()) {
            return null;
        } else {
            return (Location) sortByValueMinToMax(locationsfound).keySet().toArray()[0];
        }
    }

    public Map sortByValueMinToMax(Map unsortedMap) {
        Map sortedMap = new TreeMap(new ValueComparatorMinToMax(unsortedMap));
        sortedMap.putAll(unsortedMap);
        return sortedMap;
    }

    public Map sortByValueMaxToMin(Map unsortedMap) {
        Map sortedMap = new TreeMap(new ValueComparatorMaxToMin(unsortedMap));
        sortedMap.putAll(unsortedMap);
        return sortedMap;
    }

    class ValueComparatorMinToMax implements Comparator {

        Map map;

        public ValueComparatorMinToMax(Map map) {
            this.map = map;
        }

        public int compare(Object keyA, Object keyB) {
            Comparable valueA = (Comparable) map.get(keyA);
            Comparable valueB = (Comparable) map.get(keyB);
            return valueA.compareTo(valueB);
        }
    }

    class ValueComparatorMaxToMin implements Comparator {

        Map map;

        public ValueComparatorMaxToMin(Map map) {
            this.map = map;
        }

        public int compare(Object keyA, Object keyB) {
            Comparable valueA = (Comparable) map.get(keyA);
            Comparable valueB = (Comparable) map.get(keyB);
            return valueB.compareTo(valueA);
        }
    }

}
