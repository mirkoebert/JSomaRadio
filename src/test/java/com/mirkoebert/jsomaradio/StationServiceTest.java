package com.mirkoebert.jsomaradio;

import org.junit.jupiter.api.Test;

import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

class StationServiceTest {

    @Test
    void getSelectedStationPlsUrl() {
        StationService cut = new StationService();
        URL res = cut.getSelectedStationPlsUrl();
        assertThat(res.toString()).hasToString("https://somafm.com/nossl/secretagent.pls");
    }
}