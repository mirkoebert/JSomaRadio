package com.mirkoebert.jsomaradio;

import io.github.borewit.lizzy.playlist.Media;
import io.github.borewit.lizzy.playlist.SpecificPlaylist;
import io.github.borewit.lizzy.playlist.SpecificPlaylistFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;

@Service
@Slf4j
public class PlayListService {

    @Cacheable("urls")
    URL getAudioStreamURL(final String selectedPlsUrl) throws IOException {
        log.info("Get streaming URL from playlist");
        final SpecificPlaylist specificPlaylist = SpecificPlaylistFactory.getInstance().readFrom(new URL(selectedPlsUrl));
        specificPlaylist.toPlaylist().getRootSequence().getComponents().forEach(component -> {
            if (component instanceof Media media) {
                log.info("Media with content-source {}", media.getSource().toString());
            }
        });
        final Media first = (Media) specificPlaylist.toPlaylist().getRootSequence().getComponents().getFirst();
        return first.getSource().getURL();
    }
}
