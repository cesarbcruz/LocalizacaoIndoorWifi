/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cesar.com.localizacaoindoorapi.model;

import cesar.com.localizacaoindoorapi.core.Finder;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author cesar
 */
public class LocationTest {

    public LocationTest() {
    }

    @Test
    public void testGetNome() {
        List<Location> localizacacoes = new ArrayList<Location>();
        Location locationEscritorio = criarLocalizacaoEscritorioTeste();
        Location locationSala = criarLocalizacaoSalaTeste();
        localizacacoes.add(locationSala);
        localizacacoes.add(locationEscritorio);
        Location foundLocation = new Finder().meet(localizacacoes, getCurrentReferencesTest(-53, -75, -60));
        Assert.assertEquals(locationEscritorio.getName(), foundLocation.getName());
        foundLocation = new Finder().meet(localizacacoes, getCurrentReferencesTest(-50, -76, -60));
        Assert.assertEquals(locationSala.getName(), foundLocation.getName());
        
        Location l1 = new Location();
        l1.setName("teste");
        Location l2 = new Location();
        l2.setName("teste");
        Assert.assertTrue(l1.equals(l2));
        
        localizacacoes.clear();
        localizacacoes.add(l1);
        Assert.assertTrue(localizacacoes.contains(l2));
        

    }

    private Location criarLocalizacaoEscritorioTeste() {
        Location location = new Location();
        location.setName("escritório");
        System.out.println(location.getName());
        List<Wifi> references = new ArrayList<Wifi>();
        Wifi wifiAp1 = new Wifi();
        wifiAp1.setBssid("5A-EF-0F-36-AD-5B".toLowerCase());
        wifiAp1.setFreqInMHz(2400);
        wifiAp1.setSignalLevelInDb(-52);
        System.out.println(wifiAp1.getBssid() + ": " + wifiAp1.calculateDistance());
        references.add(wifiAp1);

        Wifi wifiAp2 = new Wifi();
        wifiAp2.setBssid("93-18-79-49-9A-BA".toLowerCase());
        wifiAp2.setFreqInMHz(2400);
        wifiAp2.setSignalLevelInDb(-75);
        System.out.println(wifiAp2.getBssid() + ": " + wifiAp2.calculateDistance());
        references.add(wifiAp2);

        Wifi wifiAp3 = new Wifi();
        wifiAp3.setBssid("B9-4E-9E-B8-18-43".toLowerCase());
        wifiAp3.setFreqInMHz(2400);
        wifiAp3.setSignalLevelInDb(-60);
        System.out.println(wifiAp3.getBssid() + ": " + wifiAp3.calculateDistance());
        references.add(wifiAp3);
        location.setReferencesAp(references);
        return location;
    }
    
    private Location criarLocalizacaoSalaTeste() {
        Location location = new Location();
        location.setName("sala");
        System.out.println(location.getName());
        List<Wifi> references = new ArrayList<Wifi>();
        Wifi wifiAp1 = new Wifi();
        wifiAp1.setBssid("5A-EF-0F-36-AD-5B".toLowerCase());
        wifiAp1.setFreqInMHz(2400);
        wifiAp1.setSignalLevelInDb(-50);
        System.out.println(wifiAp1.getBssid() + ": " + wifiAp1.calculateDistance());
        references.add(wifiAp1);

        Wifi wifiAp2 = new Wifi();
        wifiAp2.setBssid("93-18-79-49-9A-BA".toLowerCase());
        wifiAp2.setFreqInMHz(2400);
        wifiAp2.setSignalLevelInDb(-76);
        System.out.println(wifiAp2.getBssid() + ": " + wifiAp2.calculateDistance());
        references.add(wifiAp2);

        Wifi wifiAp3 = new Wifi();
        wifiAp3.setBssid("B9-4E-9E-B8-18-43".toLowerCase());
        wifiAp3.setFreqInMHz(2400);
        wifiAp3.setSignalLevelInDb(-61);
        System.out.println(wifiAp3.getBssid() + ": " + wifiAp3.calculateDistance());
        references.add(wifiAp3);
        location.setReferencesAp(references);
        return location;
    }
    
    private List<Wifi> getCurrentReferencesTest(double signalLevelInDb1, double signalLevelInDb2, double signalLevelInDb3) {
        List<Wifi> references = new ArrayList<Wifi>();
        Wifi wifiAp1 = new Wifi();
        wifiAp1.setBssid("5A-EF-0F-36-AD-5B".toLowerCase());
        wifiAp1.setFreqInMHz(2400);
        wifiAp1.setSignalLevelInDb(signalLevelInDb1);
        System.out.println(wifiAp1.getBssid() + ": " + wifiAp1.calculateDistance());
        references.add(wifiAp1);

        Wifi wifiAp2 = new Wifi();
        wifiAp2.setBssid("93-18-79-49-9A-BA".toLowerCase());
        wifiAp2.setFreqInMHz(2400);
        wifiAp2.setSignalLevelInDb(signalLevelInDb2);
        System.out.println(wifiAp2.getBssid() + ": " + wifiAp2.calculateDistance());
        references.add(wifiAp2);

        Wifi wifiAp3 = new Wifi();
        wifiAp3.setBssid("B9-4E-9E-B8-18-43".toLowerCase());
        wifiAp3.setFreqInMHz(2400);
        wifiAp3.setSignalLevelInDb(signalLevelInDb3);
        System.out.println(wifiAp3.getBssid() + ": " + wifiAp3.calculateDistance());
        references.add(wifiAp3);

        return references;
    }

}
