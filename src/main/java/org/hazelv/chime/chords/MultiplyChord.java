package org.hazelv.chime.chords;

import org.hazelv.chime.Chord;

import static org.hazelv.chime.Main.song;

public class MultiplyChord implements Chord {
    @Override
    public void execute() {
        song.currentValue = song.arguments[0] * song.arguments[1];
    }

    @Override
    public void increment() {
        song.index = song.index + 3;
    }
}
