package org.hazelv.chime;
import org.hazelv.chime.chords.*;

import javax.sound.midi.*;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static org.hazelv.chime.NoteName.*;
import static org.hazelv.chime.NoteName.C7;
import static org.hazelv.chime.NoteName.E7;
import static org.hazelv.chime.NoteName.G7;

public class Main {
    public static List<NoteEvent> noteEvents;
    public static HashMap<List<NoteName>, Class<?>> chordMap;
    public static List<Chord> chords;
    public static int bpm;
    public static Song song;
    public static boolean debug = false;

    public static void main(String[] args) {
        noteEvents = new ArrayList<>();
        chords = new ArrayList<>();
        chordMap = new HashMap<>();
        registerDefaultChords();
        if (args.length < 1) {throw new IllegalStateException("A filename must be provided");}
        if (args.length >= 2) {
            if (Objects.equals(args[1], "--debug")) {
                debug = true;
            }
        }
        try {
            File file = new File(args[0]);
            //File file = new File("test/Test2.mid");
            Sequence sequence = MidiSystem.getSequence(file);
            long res = sequence.getTickLength();
            for (Track track : sequence.getTracks()) {
                for (int i = 0; i < track.size(); i++) {
                    MidiEvent event = track.get(i);
                    MidiMessage message = event.getMessage();
                    NoteEvent note = new NoteEvent(event, message);
                    if (note.action != -1) {
                        noteEvents.add(note);
                    }
                }
            }
            noteEvents.sort(Comparator.comparingInt(NoteEvent::getTimestamp));
            bpm = noteEvents.getFirst().bpm;
            //System.out.println("bpm: "+bpm);
            for (int i = 0; i < res; i++) {
                int finalI = i;
                List<NoteEvent> timedEvents = noteEvents.stream().filter(item -> item.getTimestamp() == finalI && item.action != 0).toList();
                if (!timedEvents.isEmpty()) {
                    List<NoteName> notes = new ArrayList<>();
                    for (NoteEvent event : timedEvents) {
                            notes.add(NoteName.values()[event.getNoteNumber()]);
                    }
                    notes= notes.stream().distinct().collect(Collectors.toList());
                    notes.sort(Comparator.comparingInt(NoteName::ordinal));
                    if (chordMap.containsKey(notes)) {
                        chords.add((Chord)chordMap.get(notes).getDeclaredConstructor().newInstance());
                    } else if (notes.size() == 1) {
                        chords.add(new LiteralChord(notes.getFirst().ordinal()));
                    } else {
                        throw new IllegalArgumentException("Unknown Chord: " + notes + " at index: " + i);
                    }
                }
            }
            //song = songFromChords(chords);
            song.parse();
            song.run();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    //public static Song songFromChords(List<Chord> chords) {
        //return new Song(chords, debug);
    //}

    public static void registerDefaultChords() {
        registerChord(new ArrayList<>(Arrays.asList(C3, Eb3, G3, C7, E7, G7)), StartChord.class);
        registerChord(new ArrayList<>(Arrays.asList(D5, Gb5, A5)), Start2Chord.class);
        registerChord(new ArrayList<>(Arrays.asList(D5, Gb5, A5)), AddChord.class);
        registerChord(new ArrayList<>(Arrays.asList(E5, Ab5, B5)), SubtractChord.class);
    }

    public static void registerChord(List<NoteName> notes, Class<?> chordType) {
        chordMap.put(notes, chordType);
    }
}