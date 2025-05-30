package org.hazelv.chime.chords;

import org.hazelv.chime.Chord;
import org.hazelv.chime.NoteName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hazelv.chime.NoteName.*;

public class StartChord implements Chord {
    public List<NoteName> notes;

    StartChord() {
        notes = new ArrayList<>(Arrays.asList(C3, E3, G3, C5, E5, G5));
    }


    @Override
    public void execute() {

    }

    @Override
    public void increment() {

    }
}
