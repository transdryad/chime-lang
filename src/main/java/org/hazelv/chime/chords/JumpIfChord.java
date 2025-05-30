package org.hazelv.chime.chords;

import org.hazelv.chime.Chord;

import java.io.IOException;

import static org.hazelv.chime.Main.song;

public class JumpIfChord implements Chord {
    @Override
    public void execute() throws IOException {
        if (song.chords.size() <= song.arguments[0]) {
            throw new IndexOutOfBoundsException("Jump index out of range for jump from chord: " + song.index + " to chord: " + song.arguments[0]);
        }
        if (song.currentValue == song.arguments[1]) {
            song.index = (int)song.arguments[0];
        } else {
            song.index = song.index + 3;
        }
    }

    @Override
    public void increment() {
    }
}
