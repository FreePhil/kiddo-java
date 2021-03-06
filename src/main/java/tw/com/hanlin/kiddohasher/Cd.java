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
public class Cd {
    private String album;
    private List<Track> tracks = new ArrayList<>();
}
