package com.cesar.localizacaoindoorandroid;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.util.ArrayList;
import java.util.List;

import cesar.com.localizacaoindoorapi.model.Wifi;

/**
 * Created by cesar on 15/01/17.
 */
public class ResourceWifi {

    private WifiManager wifiManager;

    public ResourceWifi(Context context){
            wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    public List<Wifi> scan(){
        List<Wifi> wifiReferences = new ArrayList<Wifi>();
        wifiManager.startScan();
        List<ScanResult> scanResults = wifiManager.getScanResults();
        for (final ScanResult scanResult : scanResults) {
            Wifi wifiReference = new Wifi();
            wifiReference.setBssid(scanResult.BSSID);
            wifiReference.setFreqInMHz(scanResult.frequency);
            wifiReference.setSignalLevelInDb(scanResult.level);
            wifiReferences.add(wifiReference);
            //System.out.println(scanResult.SSID+" - "+wifiReference.calculateDistance());
        }
        return wifiReferences;
    }
}
