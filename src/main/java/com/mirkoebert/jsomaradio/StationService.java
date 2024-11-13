package com.mirkoebert.jsomaradio;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.prefs.Preferences;

@Service
@Slf4j
class StationService {

    private static final String somaBaseUrl = "https://somafm.com/nossl/";
    private static final String plsExtension = ".pls";
    @Getter
    private final String[] stationsNames = {
            "Beat Bender", "Black Rock FM", "Boot Liquor", "Bossa Beyond",
            "Chilitis Radio", "Christmas Lounge",
            "Groove Salad",
            "Drone Zone", "Deep Space One",
            "Indie Pop Rocks!",
            "Secret Agent",
            "PopTron"};
    private final String[] stationsPls = {
            "beatblender", "brfm", "bootliquor", "bossa",
            "chillits", "christmas",
            "groovesalad",
            "dronezone", "deepspaceone",
            "indiepop",
            "secretagent",
            "poptron"};

    private Preferences prefs = Preferences.userNodeForPackage(getClass());
    @Getter
    private int selectedStationIndex;

    StationService() {
        selectedStationIndex = prefs.getInt("selectedStationIndex", 0);
        log.info("Restore last select station index {}", selectedStationIndex);
    }


    URL getSelectedStationPlsUrl() {
        return getStationPlsUrl(selectedStationIndex);
    }

    void setSelectedStationIndex(int i) {
        prefs.putInt("selectedStationIndex", i);
    }

    private URL getStationPlsUrl(int i) {
        try {
            return new URI(somaBaseUrl + stationsPls[i] + plsExtension).toURL();
        } catch (MalformedURLException | URISyntaxException e) {
            log.error("Can't build station url for index {}", i);
            throw new RuntimeException("Can't build station url for index", e);
        }
    }
}
