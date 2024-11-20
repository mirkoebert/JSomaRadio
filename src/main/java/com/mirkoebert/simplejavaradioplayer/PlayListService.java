package com.mirkoebert.simplejavaradioplayer;

import io.github.borewit.lizzy.playlist.Media;
import io.github.borewit.lizzy.playlist.SpecificPlaylist;
import io.github.borewit.lizzy.playlist.SpecificPlaylistFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
class PlayListService {

    private final Map<URL, URL> cache = new HashMap<>();

    URL getAudioStreamURL(final URL selectedPlsUrl) {
        final URL cachedUrl = cache.get(selectedPlsUrl);

        if (cachedUrl == null) {
            log.info("Cache miss. Retrieve stream url for select Playlist URL {}", selectedPlsUrl);
            final URL directUrl = getAudioStreamURLdirect(selectedPlsUrl);
            cache.put(selectedPlsUrl, directUrl);
            log.info("Retrieved stream url {}", directUrl);
            return directUrl;
        }
        log.info("Cache hit. Found stream url {} for playlist url {}", cachedUrl, selectedPlsUrl);
        return cachedUrl;
    }

    private URL getAudioStreamURLdirect(final URL selectedPlsUrl) {
        try {
            log.info("Get stream URL from playlist {}", selectedPlsUrl);
            final SpecificPlaylist specificPlaylist = SpecificPlaylistFactory.getInstance().readFrom(selectedPlsUrl);
            specificPlaylist.toPlaylist().getRootSequence().getComponents().forEach(component -> {
                if (component instanceof Media media) {
                    log.info("Media with content-source {}", media.getSource().toString());
                }
            });
            final Media first = (Media) specificPlaylist.toPlaylist().getRootSequence().getComponents().getFirst();
            return first.getSource().getURL();
        } catch (Exception e) {
            log.warn("Can't get stream url for playlist", e);
            return null;
        }
    }


}
