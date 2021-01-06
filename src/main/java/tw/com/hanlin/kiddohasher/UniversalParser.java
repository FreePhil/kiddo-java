package tw.com.hanlin.kiddohasher;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class UniversalParser implements ParserInterface {

    private final static Logger log = LoggerFactory.getLogger(UniversalParser.class);

    final
    MediaParserInterface mp3Parser;

    final
    MediaParserInterface wavParser;

    public UniversalParser(@Qualifier("mp3MediaParser") MediaParserInterface mp3Parser, @Qualifier("wavMediaParser") MediaParserInterface wavParser) {
        this.mp3Parser = mp3Parser;
        this.wavParser = wavParser;
    }

    @Override
    public int getAudioLength(File file) {
        String filename = file.getName();
        String extension = FilenameUtils.getExtension(filename);

        MediaParserInterface parser;
        switch (extension) {
            case "mp3":
                parser = this.mp3Parser;
                break;
            default:
                parser = this.wavParser;
                break;
        }

        int duration = 0;
        try {
            duration = parser.getDuration(file);
            log.info("universal log: {}", duration);
        }
        catch (Exception e) {}

        return duration;
    }
}