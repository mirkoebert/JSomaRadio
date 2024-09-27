package com.mirkoebert.jsomaradio;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.prefs.Preferences;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

@Service
@Slf4j
class StationService {

    private static final String somaBaseUrl = "https://somafm.com/nossl/";
    private static final String plsExtension = ".pls";
    @Getter
    private final String[] stationsNames = {"Groove Salad","Drone Zone","Deep Space One","Indie Pop Rocks!","Secret Agent", "PopTron"};
    private final String[] stationsPls = {"groovesalad","dronezone","deepspaceone","indiepop","secretagent", "poptron"};

    private Preferences prefs = Preferences.userNodeForPackage(getClass());
    @Getter
    private int selectedStationIndex;

    StationService(){
        selectedStationIndex = prefs.getInt("selectedStationIndex", 0);
        log.info("Restore last select station index {}", selectedStationIndex);
    }


    URL getSelectedStationPlsUrl() {
        return getStationPlsUrl(selectedStationIndex);
    }

    void setSelectedStationIndex(int i) {
        prefs.putInt("selectedStationIndex", i);
    }

    public URL getStationPlsUrl(int i) {
        try {
            return new URI(somaBaseUrl + stationsPls[i] + plsExtension).toURL();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
