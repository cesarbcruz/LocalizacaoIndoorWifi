/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cesar.com.localizacaoindoorapi.model;

/**
 *
 * @author cesar
 */
public class Wifi {

    private String bssid;
    private double signalLevelInDb;
    private double freqInMHz;

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public double getSignalLevelInDb() {
        return signalLevelInDb;
    }

    public void setSignalLevelInDb(double signalLevelInDb) {
        this.signalLevelInDb = signalLevelInDb;
    }

    public double getFreqInMHz() {
        return freqInMHz;
    }

    public void setFreqInMHz(double freqInMHz) {
        this.freqInMHz = freqInMHz;
    }
    
    

    public double calculateDistance() {
        double exp = (27.55 - (20 * Math.log10(freqInMHz)) + Math.abs(signalLevelInDb)) / 20.0;
        return Math.pow(10.0, exp);
    }
    
    @Override
    public String toString() {
        return this.getBssid();
    }
}
