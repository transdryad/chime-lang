package org.hazelv.chime;
import javax.sound.midi.*;
import java.io.File;
import java.util.*;

public class Main {
    public static List<NoteEvent> noteEvents;
    public static HashMap<List<NoteName>, Chord> chordMap;
    public static List<Chord> chords;
    public static int bpm;
    public static Song song;
    public static boolean debug = false;

    public static void main(String[] args) {
        noteEvents = new ArrayList<>();
        chords = new ArrayList<>();
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
                    //chords.add(notes);
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
}