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
            } else if (Objects.equals(chord, new ArrayList<>(Arrays.asList(C7, E7, G7, C3, Eb3, G3)))) {
                code.add(ChordName.START2);
            } else if (Objects.equals(chord, new ArrayList<>(Arrays.asList(D5, Gb5, A5)))) {
                code.add(ChordName.ADD);
            }
        }
    }

    public void run() {
        if (!code.getFirst().equals(ChordName.START) || !code.get(1).equals(ChordName.START2)) {
            System.out.println(chords);
            throw new IllegalArgumentException("Invalid start sequence. (Are you sure this is a program?)");
        }
    }

    @Override
    public String toString() {
        return chords.toString();
    }
}
