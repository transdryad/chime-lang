package org.hazelv.chime.chords;

import org.hazelv.chime.Chord;

import java.io.IOException;

import static org.hazelv.chime.Main.song;

public class EvalChord implements Chord {
    @Override
    public void execute() throws IOException {
        if (song.arguments[0] < song.arguments[1]) {
            song.currentValue = 1;
        } else if (song.arguments[0] == song.arguments[1]) {
            song.currentValue = 2;
        } else if (song.arguments[0] > song.arguments[1]) {
            song.currentValue = 3;
        }
    }

    @Override
    public void increment() {
        song.index = song.index + 3;
    }
}
