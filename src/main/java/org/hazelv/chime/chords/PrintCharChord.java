package org.hazelv.chime.chords;

import org.hazelv.chime.Chord;

import java.io.IOException;

import static org.hazelv.chime.Main.song;

public class PrintCharChord implements Chord {
    @Override
    public void execute() throws IOException {
        System.out.print((char)song.currentValue);
    }

    @Override
    public void increment() {
        song.index++;
    }
}
