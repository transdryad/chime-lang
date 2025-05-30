package org.hazelv.chime.chords;

import org.hazelv.chime.Chord;
import org.hazelv.chime.Main;

public class Start2Chord implements Chord {

    @Override
    public void execute() {
    }

    @Override
    public void increment() {
        Main.song.index++;
    }
}
