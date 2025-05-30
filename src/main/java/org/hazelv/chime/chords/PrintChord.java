package org.hazelv.chime.chords;

import org.hazelv.chime.Chord;

import static org.hazelv.chime.Main.song;

public class PrintChord implements Chord {
    @Override
    public void execute() {
        System.out.println(song.currentValue);
    }

    @Override
    public void increment() {
        song.index = song.index + 1;
    }
}
