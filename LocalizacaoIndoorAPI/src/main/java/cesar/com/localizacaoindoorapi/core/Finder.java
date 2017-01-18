/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cesar.com.localizacaoindoorapi.core;

import cesar.com.localizacaoindoorapi.model.Location;
import cesar.com.localizacaoindoorapi.model.Wifi;
import java.util.ArrayList;
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
        Map<Location, Double> locationsfound = new HashMap<>();
        for (Location location : persistentLocations) {
            for (Wifi currentReference : currentReferences) {
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
            locationsfound = sortByValueMinToMax(locationsfound);
            return (Location) locationsfound.keySet().toArray()[0];
        }
    }

    public List<Wifi> average(List<Wifi> currentReferences) {
        Map<String, List<Wifi>> mapReferences = new HashMap<>();
        for (Wifi ref : currentReferences) {
            if (mapReferences.containsKey(ref.getBssid())) {
                mapReferences.get(ref.getBssid()).add(ref);
            } else {
                List<Wifi> newRef = new ArrayList<>();
                newRef.add(ref);
                mapReferences.put(ref.getBssid(), newRef);
            }
        }
        
        List<Wifi> newReferences = new ArrayList<>();
        
        for (Map.Entry<String, List<Wifi>> ref : mapReferences.entrySet()) {
            double sumLevelInDb = 0D;
            for (Wifi wifi : ref.getValue()) {
                sumLevelInDb += wifi.getSignalLevelInDb();
            }
            ref.getValue().get(0).setSignalLevelInDb(sumLevelInDb = sumLevelInDb/ref.getValue().size());
            newReferences.add(ref.getValue().get(0));
        }
        
        return newReferences;

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
