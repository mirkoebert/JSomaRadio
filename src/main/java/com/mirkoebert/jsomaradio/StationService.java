package com.mirkoebert.jsomaradio;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

@Service
@Slf4j
class StationService {

    private static final String somaBaseUrl = "https://somafm.com/nossl/";
    @Getter
    private final String[] stationsNames = {"Secret Agent", "PopTron"};
    private final String[] stationsPls = {"secretagent.pls", "poptron.pls"};

    @Setter
    private int selectedStationIndex = 0;

    URL getSelectedStationPlsUrl() {
        try {
            return new URI(somaBaseUrl + stationsPls[selectedStationIndex]).toURL();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
