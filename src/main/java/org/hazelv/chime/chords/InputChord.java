package org.hazelv.chime.chords;

import org.hazelv.chime.Chord;

import java.io.IOException;

import static org.hazelv.chime.Main.song;

public class InputChord implements Chord {
    @Override
    public void execute() throws IOException {
        song.currentValue = (float)System.in.read();
    }
    @Override
    public void increment() {
        song.index++;
    }
}
