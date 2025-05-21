import javax.sound.midi.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static List<NoteEvent> noteEvents = new ArrayList<>();

    public static void main(String[] args) {
        //if (args.length < 1) {throw new IllegalStateException("A filename must be provided");}
        //File file = new File(args[0]);
        File file = new File("./Test.mid");
        try {
            Sequence sequence = MidiSystem.getSequence(file);
            for (Track track : sequence.getTracks()) {
                for (int i = 0; i < track.size(); i++) {
                    MidiEvent event = track.get(i);
                    MidiMessage message = event.getMessage();
                    NoteEvent note = new NoteEvent(event, message);
                    if (!(note.action == -1)) {
                        noteEvents.add(note);
                    }
                }
            }
            System.out.println(noteEvents);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
