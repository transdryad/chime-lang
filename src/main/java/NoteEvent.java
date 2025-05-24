import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;

public class NoteEvent{
    public MidiEvent midiEvent;
    public MidiMessage midiMessage;
    public long timestamp;
    public int action = -1;
    public int noteNumber = -1;
    public int velocity = -1;
    public int bpm = -1;

    public NoteEvent(MidiEvent event, MidiMessage message) {
        this.midiEvent = event;
        this.midiMessage = message;
        this.timestamp = this.midiEvent.getTick();
        if (message instanceof ShortMessage shortMessage) {
            if (shortMessage.getCommand() == ShortMessage.NOTE_ON && shortMessage.getData2() != 0) {
                this.action = 1;
                this.noteNumber = shortMessage.getData1();
                this.velocity = shortMessage.getData2();
            } else if (shortMessage.getCommand() == ShortMessage.NOTE_OFF || shortMessage.getData2() == 0) {
                System.out.println("Note Off");
                this.action = 0;
                this.noteNumber = shortMessage.getData1();
                this.velocity = shortMessage.getData2();
            }
        } else if (message instanceof MetaMessage mm) {
            if(mm.getType()==0x51){
                this.action = 2;
                this.timestamp = -1;
                byte[] data = mm.getData();
                int tempo = (data[0] & 0xff) << 16 | (data[1] & 0xff) << 8 | (data[2] & 0xff);
                bpm = 60000000 / tempo;
            }
        }
    }

    public String toString() {
        if (this.action==2) {
            return "Metadata event: bpm=" + this.bpm;
        } else {
            return "(Note  " + this.action + ": Key=" + this.noteNumber + ", Velocity=" + this.velocity + ", PIANO key=" + (this.noteNumber - 20) + ", Timestamp=" + this.timestamp + ")";
        }
    }

    public int getTimestamp() {
        return Math.toIntExact(this.timestamp);
    }

    public int getNoteNumber() {
        return this.noteNumber;
    }
}
