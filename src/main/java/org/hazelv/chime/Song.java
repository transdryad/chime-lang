package org.hazelv.chime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import static org.hazelv.chime.NoteName.*;

public class Song {
    public List<List<NoteName>> chords;
    public List<ChordName> code = new ArrayList<>();

    public Song(List<List<NoteName>> chords) {
        this.chords = chords;
    }

    public void parse() {
        for (List<NoteName> chord : chords) {
            if (Objects.equals(chord, new ArrayList<>(Arrays.asList(C5, E5, G5, C3, E3, G3)))) {
                code.add(ChordName.START);
            }
        }
    }

    public void run() {
        if (!code.getFirst().equals(ChordName.START)) {
            throw new IllegalArgumentException("The first chord wasn't START. (Are you sure this is a chime program?)");
        }
    }

    @Override
    public String toString() {
        return chords.toString();
    }
}
