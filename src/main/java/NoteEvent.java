import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;

public class NoteEvent {
    public MidiEvent midiEvent;
    public MidiMessage midiMessage;
    public int action;
    public int noteNumber;
    public int velocity;

    public NoteEvent(MidiEvent event, MidiMessage message) {
        this.midiEvent = event;
        this.midiMessage = message;
        if (message instanceof ShortMessage shortMessage) {
            if (shortMessage.getCommand() == ShortMessage.NOTE_ON) {
                this.action = 1;
                this.noteNumber = shortMessage.getData1();
                this.velocity = shortMessage.getData2();
                //System.out.println("Note  " + this.action + ": Key=" + this.noteNumber + ", Velocity=" + this.velocity);
            } else if (shortMessage.getCommand() == ShortMessage.NOTE_OFF) {
                this.action = 0;
                this.noteNumber = shortMessage.getData1();
                this.velocity = shortMessage.getData2();
            }
        } else {
            this.action = -1;
            this.noteNumber = -1;
            this.velocity = -1;
        }
    }

    public String toString() {
        return "Note  " + this.action + ": Key=" + this.noteNumber + ", Velocity=" + this.velocity + ", PIANO key=" + (this.noteNumber-20);
    }
}
