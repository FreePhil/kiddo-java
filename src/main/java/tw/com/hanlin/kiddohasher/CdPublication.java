package tw.com.hanlin.kiddohasher;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CdPublication {
    private String volume;
    private String volumeImage;
    private String volumeImagePath;
    private String urlPrefix = "https://kiddo-mp3.hle.com.tw/";
    private List<Cd> cds = new ArrayList<>();
}
