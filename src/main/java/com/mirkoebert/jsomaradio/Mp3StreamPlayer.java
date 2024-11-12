package com.mirkoebert.jsomaradio;

import com.goxr3plus.streamplayer.stream.StreamPlayer;
import com.goxr3plus.streamplayer.stream.StreamPlayerEvent;
import com.goxr3plus.streamplayer.stream.StreamPlayerListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

@Service
@Slf4j
class Mp3StreamPlayer extends StreamPlayer implements StreamPlayerListener {

    private InputStream in;

    Mp3StreamPlayer() {
        addStreamPlayerListener(this);
    }

    void playStream(final URL audioStreamUrl) {
        log.info("playStream: {}", audioStreamUrl);

        try {
            in = audioStreamUrl.openStream();
            final BufferedInputStream buf = new BufferedInputStream(in);
            open(buf);
            play();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    void playStream(final InputStream in) {
        log.info("playStream");
        try {
            open(in);
            play();
        } catch (Exception e) {
            log.error("Cant play stream", e);
            //throw new RuntimeException(e);
        }
    }

    @Override
    public void opened(Object dataSource, Map<String, Object> properties) {
        log.info("Opend: {}, {}", dataSource, properties);
    }

    @Override
    public void progress(int nEncodedBytes, long microsecondPosition, byte[] pcmData, Map<String, Object> properties) {
        //log.info("Progress: {}, {}, {}", nEncodedBytes, microsecondPosition, properties);
    }

    @Override
    public void statusUpdated(StreamPlayerEvent event) {
        log.info("Get StreamPlayerEvent: {}", event.toString());
    }

    @Override
    public void stop() {
        super.stop();
    }

}
