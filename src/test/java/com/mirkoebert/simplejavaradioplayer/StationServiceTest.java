package com.mirkoebert.simplejavaradioplayer;

import org.junit.jupiter.api.Test;

import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

class StationServiceTest {

    @Test
    void getSelectedStationPlsUrl() {
        final StationService cut = new StationService();
        final URL res = cut.getSelectedStationPlsUrl();
        assertThat(res.toString()).startsWith("https://somafm.com/nossl/");
    }
}