package org.hazelv.chime.chords;

import org.hazelv.chime.Chord;

import java.util.Stack;

import static org.hazelv.chime.Main.song;

public class PushChord implements Chord {
    @Override
    public void execute() {
        if (song.data.size() < song.arguments[0]) {
            for (int ii = 0; ii < song.arguments[0]; ii++) {
                song.data.add(new Stack<>());
            }
        }
        song.data.get((int)song.arguments[0]).push(song.currentValue);
    }

    @Override
    public void increment() {
        song.index = song.index + 2;
    }
}
