package tw.com.hanlin.kiddohasher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;

@Service
public class WavMediaParser implements MediaParserInterface {
    private final static Logger log = LoggerFactory.getLogger(WavMediaParser.class);

    @Override
    public int getDuration(File audioFile) throws Exception {

        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
        AudioFormat format = audioInputStream.getFormat();

        long audioFileLength = audioFile.length();
        int frameSize = format.getFrameSize();
        float frameRate = format.getFrameRate();
        float durationInSeconds = (audioFileLength / (frameSize * frameRate));

        log.info("wav parser: {}", durationInSeconds);

        return (Math.round(durationInSeconds));
    }
}
