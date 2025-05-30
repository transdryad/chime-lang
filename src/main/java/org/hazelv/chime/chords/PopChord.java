package org.hazelv.chime.chords;

import org.hazelv.chime.Chord;

import static org.hazelv.chime.Main.song;

public class PopChord implements Chord {
    @Override
    public void execute() {
        song.currentValue = song.data.get((int)song.arguments[0]).pop();
    }

    @Override
    public void increment() {
        song.index = song.index + 2;
    }
}
