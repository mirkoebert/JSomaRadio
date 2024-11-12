package com.mirkoebert.jsomaradio;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Service
@RequiredArgsConstructor
@Slf4j
public class StreamWatchdog {

    private final Mp3StreamPlayer mp3StreamPlayer;
    private InputStream in;
    private URL audioStreamUrl;

    void playStream(final URL audioStreamUrl) {
        this.audioStreamUrl = audioStreamUrl;
        openNewPlayerWithNewStream();
    }

    @Scheduled(fixedRate = 2000)
    void watchDog() {
        try {
            if (in == null) {
                log.warn("In stream is null");
            } else if (in.available() > 0) {
                log.info("In stream is ok");
            } else {
                log.warn("In stream is not ok");
                openNewPlayerWithNewStream();
            }
        } catch (IOException e) {
            log.warn("QQQ {}", e.getMessage());
        }
    }

    private void openNewPlayerWithNewStream() {
        try {
            in = audioStreamUrl.openStream();
//            mp3StreamPlayer.stop();
            mp3StreamPlayer.playStream(in);
        } catch (IOException e) {
            log.error("Cant play stream: {}", audioStreamUrl);
        }
    }

    public void stop() {
        mp3StreamPlayer.stop();
        try {
            in.close();
        } catch (IOException e) {
            log.warn("Cant close stream after stopping the player");
        }
    }
}
