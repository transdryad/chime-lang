package org.hazelv.chime;
import org.hazelv.chime.chords.*;

import java.io.IOException;
import java.util.*;

public class OldSong {
    public List<Chord> chords;
    public List<Stack<Float>> data;
    public float currentValue = 0.0f;
    public boolean debug;
    public int index = 0;
    public float[] arguments;

    public OldSong(List<Chord> chords, boolean debug) {
        this.chords = chords;
        this.data = new ArrayList<>();
        this.debug = debug;
        for (int i = 0; i < 2; i++) {
            data.add(new Stack<>());
        }
    }

    public void run() throws IOException {
        if (debug) {
            System.out.println(chords);
        }
        if (!chords.getFirst().equals(new StartChord()) || !chords.get(1).equals(new Start2Chord())) {
            //System.out.println(chords);
            throw new IllegalArgumentException("Invalid 'header' for chime file. (Are you sure this is a program?)");
        }
        //System.out.println(code);
        for (index = 0; index < code.size(); index++) {
            Object instruction = code.get(index);
            //System.out.println(instruction);
            if (instruction.equals(ChordName.START) || instruction.equals(ChordName.START2)) {
                if (index > 1) {
                    throw new IllegalArgumentException("Start sequence repeated?");
                }
                index++;
                continue;
            } else if (instruction instanceof Integer || instruction instanceof Float) {
                throw new IllegalArgumentException("Literal before instruction at chord: " + index + ". Bad jump?");
            }
            arguments = getArguments(index);
            switch (instruction) {
                case ChordName.ADD:
                    currentValue = arguments[0] + arguments[1];
                    index = index + 2;
                    break;
                case ChordName.SUBTRACT:
                    //System.out.println("Subtracting: " + arguments[0] + " - " + arguments[1]);
                    currentValue = arguments[0] - arguments[1];
                    //System.out.println(currentValue);
                    index = index + 2;
                    break;
                case ChordName.MULTIPLY:
                    currentValue = arguments[0] * arguments[1];
                    index = index + 2;
                    break;
                case ChordName.DIVIDE:
                    currentValue = arguments[0] / arguments[1];
                    index = index + 2;
                    break;
                case ChordName.PRINT:
                    //System.out.println("Printing");
                    System.out.print(currentValue);
                    break;
                case ChordName.PUSH:
                    if (data.size() < arguments[0]) {
                        for (int ii = 0; ii < arguments[0]; ii++) {
                            data.add(new Stack<>());
                        }
                    }
                    data.get((int)arguments[0]).push(currentValue);
                    index++;
                    break;
                case ChordName.POP:
                    currentValue = data.get((int)arguments[0]).pop();
                    index++;
                    break;
                case ChordName.INPUT:
                    currentValue = (float)System.in.read();
                    break;
                case ChordName.HOLD:
                    currentValue = arguments[0];
                    index++;
                    break;
                case ChordName.PRINT_CHAR:
                    System.out.print((char)currentValue);
                    break;
                case ChordName.PRINTLN:
                    System.out.println();
                    break;
                case ChordName.EVAL:
                    if (arguments[0] < arguments[1]) {
                        currentValue = 1;
                    } else if (arguments[0] == arguments[1]) {
                        currentValue = 2;
                    } else if (arguments[0] > arguments[1]) {
                        currentValue = 3;
                    }
                    index = index + 2;
                    break;
                case ChordName.JUMP: // chord count starts at 0
                    if (code.size() <= arguments[0]) {throw new IndexOutOfBoundsException("Jump index out of range for jump from chord: " + index + " to chord: " + arguments[0]);}
                    index = (int)(arguments[0] - 1); // subtract one as for loop will add one at the end
                    break;
                case ChordName.JUMP_IF: // first argument is jump chord, second argument is wanted condition from eval or otherwise
                    if (code.size() <= arguments[0]) {throw new IndexOutOfBoundsException("Jump index out of range for jump from chord: " + index + " to chord: " + arguments[0]);}
                    if (currentValue == arguments[1]) {
                        index = (int)(arguments[0] - 1);
                    } else {
                        index = index + 2;
                    }
                    break;
                default:
                    throw new IllegalStateException("Unknown Instruction: " + instruction + " at chord: " + index);
            }
        }

    }

    @Override
    public String toString() {
        return chords.toString() + " -> " + code.toString();
    }

    public float[] getArguments(int index) {
        if (index == (code.size() - 1)) { // end of list
            return new float[]{};
        } else if (index == (code.size() - 2)) { // one arg until end
            if (code.get(index + 1).equals(ChordName.CURRENT_VALUE)) {
                return new float[]{currentValue};
            } else {
                return new float[]{getArgument(index + 1)};
            }
        } else if (index <= (code.size() - 3)) {
            if (code.get(index + 1).equals(ChordName.CURRENT_VALUE)) {
                return new float[]{currentValue, getArgument(index + 2)};
            } else if (code.get(index + 2).equals(ChordName.CURRENT_VALUE)) {
                return new float[]{getArgument(index + 1), currentValue};
            } else if (code.get(index + 1).equals(ChordName.CURRENT_VALUE) && code.get(index + 2).equals(ChordName.CURRENT_VALUE)) {
                return new float[]{currentValue, currentValue};
            } else {
                return new float[]{getArgument(index + 1), getArgument(index + 2)};
            }
        } else {
            return new float[]{};
        }
    }

    public float getArgument(int index) {
        if (code.get(index) instanceof ChordName) {
            return Float.NaN;
        } else {
            return (float)code.get(index);
        }
    }
}
