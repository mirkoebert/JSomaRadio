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

    private URL currenAudioStreamUrl;

    Mp3StreamPlayer() {
        addStreamPlayerListener(this);
    }

    void playStream(final URL audioStreamUrl)  {
        log.info("playStream: {}", audioStreamUrl);
        if (stopStream(audioStreamUrl)){
            stop();
            log.info("Stop playing Stream {}.", currenAudioStreamUrl);
            currenAudioStreamUrl = null;
            return;
        }
        stop();
        currenAudioStreamUrl = audioStreamUrl;

        try {
            final InputStream in = audioStreamUrl.openStream();
            final BufferedInputStream buf = new BufferedInputStream(in);
            open(buf);
            play();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    boolean stopStream(final URL audioStreamUrl){
        if (currenAudioStreamUrl == null){
            return false;
        }
        if (audioStreamUrl.toString().equals(currenAudioStreamUrl.toString())){
            return true;
        }
        return false;
    }

    @Override
    public void opened(Object dataSource, Map<String, Object> properties) {

            }

    @Override
    public void progress(int nEncodedBytes, long microsecondPosition, byte[] pcmData, Map<String, Object> properties) {

    }

    @Override
    public void statusUpdated(StreamPlayerEvent event) {

    }
}
