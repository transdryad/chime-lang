package org.hazelv.chime.chords;

import org.hazelv.chime.Chord;

public class LiteralChord implements Chord {
    public final float value;

    public LiteralChord(float value) {
        this.value = value;
    }
    @Override
    public void execute() {
    }

    @Override
    public void increment() {
    }
}
