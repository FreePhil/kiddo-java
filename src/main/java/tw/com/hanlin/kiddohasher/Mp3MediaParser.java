package tw.com.hanlin.kiddohasher;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class Mp3MediaParser implements MediaParserInterface {
    private final static Logger log = LoggerFactory.getLogger(Mp3MediaParser.class);

    @Override
    public int getDuration(File audioFile) throws Exception {

        int duration = 0;

        AudioFile audioParser = AudioFileIO.read(audioFile);
        duration = audioParser.getAudioHeader().getTrackLength();
        log.info("mp3 parser: {}", duration);

        return duration;
    }
}
