package tw.com.hanlin.kiddohasher;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@SpringBootApplication
public class KiddoHasherApplication implements CommandLineRunner {

    private final static Logger log = LoggerFactory.getLogger(KiddoHasherApplication.class);

    @Autowired
    ParserInterface parser;

    public static void main(String[] args) {
        log.info("starting the application");
        SpringApplication.run(KiddoHasherApplication.class, args);
        log.info("done with the application");
    }

    @Override
    public void run(String... args) throws Exception {

        log.info("execute the command");

        String mp3RootFolder = args[0];
//        String copyDestinationFolder = args[1];
        MessageDigest sha1HashWorker = MessageDigest.getInstance("SHA-1");
        FilenameFilter directoryFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return new File(dir, name).isDirectory();
            }
        };
        FilenameFilter fileFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return new File(dir, name).isFile();
            }
        };

        File fileEnumerator = new File(mp3RootFolder);
        String[] directories;
        directories = fileEnumerator.list(directoryFilter);

        assert directories != null;
        for (String directory: directories) {
            log.info("directory: {}", directory);

            CdPublication publication = new CdPublication();
            String publicationFolder = String.format("%s/%s", mp3RootFolder, directory);
            File imageFileEnumerator = new File(publicationFolder);
            String imageFilename = Arrays.stream(Objects.requireNonNull(imageFileEnumerator.list(fileFilter))).findFirst().get();

            String imagePath = String.format("%s/%s", publicationFolder, imageFilename);
            log.info("thumbnail image: {}", imagePath);

            publication.setVolumeImagePath(imagePath);
            publication.setVolumeImage(publication.getUrlPrefix() + imageFilename);
            publication.setVolume(directory);

            List<String> cds = Arrays.stream(Objects.requireNonNull(imageFileEnumerator.list(directoryFilter)))
                    .sorted().collect(Collectors.toList());
            for (String cdName: cds) {
                String cdPath = String.format("%s/%s", publicationFolder, cdName);
                log.info("cd path: {}", cdPath);

                Cd cd = new Cd();
                cd.setAlbum(cdName);

                File mp3File = new File(cdPath);
                List<String> mp3Names = Arrays.stream(Objects.requireNonNull(mp3File.list(fileFilter)))
                        .sorted().collect(Collectors.toList());
                for (String mp3Name: mp3Names) {
                    String filePath = String.format("%s/%s", cdPath, mp3Name);
                    log.info("mp3 path: {}", filePath);

                    Track track = new Track();
                    track.setTitle(mp3Name);

                    String filenameOnly = FilenameUtils.removeExtension(mp3Name);
                    String hashedFilenameOnly = DigestUtils.sha1Hex(filenameOnly.getBytes(StandardCharsets.UTF_8));
                    String url = String.format("%s%s.%s",
                            publication.getUrlPrefix(), hashedFilenameOnly, FilenameUtils.getExtension(mp3Name));
                    track.setUrl(url);

                    int duration = 0;
                    duration = parser.getAudioLength(new File(filePath));
                    track.setDuration(String.format("%d", duration));

                    log.info("url of mp3 file: {}, duration: {}", url, duration);

                    cd.getTracks().add(track);
                }


                publication.getCds().add(cd);
            }
            // output the json format of publication
            //
        }
    }
}
