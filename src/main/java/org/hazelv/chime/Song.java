package org.hazelv.chime;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.hazelv.chime.NoteName.*;

public class Song {
    public List<List<NoteName>> chords;
    public List<Object> code;
    public List<Stack<Float>> data;
    public float currentValue = 0.0f;
    public boolean debug;
    public int index = 0;

    public Song(List<List<NoteName>> chords, boolean debug) {
        this.chords = chords;
        this.code = new ArrayList<>();
        this.data = new ArrayList<>();
        this.debug = debug;
        for (int i = 0; i < 2; i++) {
            data.add(new Stack<>());
        }
    }

    public void parse() {
        index = 0;
        for (List<NoteName> chord : chords) {
            chord = chord.stream().distinct().collect(Collectors.toList());
            chord.sort(Comparator.comparingInt(NoteName::ordinal));
            if (Objects.equals(chord, new ArrayList<>(Arrays.asList(C3, E3, G3, C5, E5, G5)))) {
                code.add(ChordName.START);
            } else if (Objects.equals(chord, new ArrayList<>(Arrays.asList(C3, Eb3, G3, C7, E7, G7)))) {
                code.add(ChordName.START2);
            } else if (Objects.equals(chord, new ArrayList<>(Arrays.asList(D5, Gb5, A5)))) {
                code.add(ChordName.ADD);
            } else if (Objects.equals(chord, new ArrayList<>(Arrays.asList(E5, Ab5, B5)))) {
                code.add(ChordName.SUBTRACT);
            } else if (Objects.equals(chord, new ArrayList<>(Arrays.asList(F5, A5, C6)))) {
                code.add(ChordName.MULTIPLY);
            } else if (Objects.equals(chord, new ArrayList<>(Arrays.asList(G5, B5, D6)))) {
                code.add(ChordName.DIVIDE);
            } else if (Objects.equals(chord, new ArrayList<>(Arrays.asList(A5, Db6, E6)))) {
                code.add(ChordName.PRINT);
            } else if (Objects.equals(chord, new ArrayList<>(Arrays.asList(B5, Eb6, Gb6)))) {
                code.add(ChordName.PUSH);
            } else if (Objects.equals(chord, new ArrayList<>(Arrays.asList(Bb5, D6, F6)))) {
                code.add(ChordName.POP);
            } else if (Objects.equals(chord, new ArrayList<>(Arrays.asList(Db5, F5, Ab5)))) {
                code.add(ChordName.INPUT);
            } else if (Objects.equals(chord, new ArrayList<>(Arrays.asList(Eb5, G5, Bb5)))) {
                code.add(ChordName.HOLD);
            } else if (Objects.equals(chord, new ArrayList<>(Arrays.asList(Gb5, Bb5, Db6)))) {
                code.add(ChordName.PRINT_CHAR);
            } else if (Objects.equals(chord, new ArrayList<>(Arrays.asList(Ab5, C6, Eb6)))) {
                code.add(ChordName.PRINTLN);
            } else if (Objects.equals(chord, new ArrayList<>(Arrays.asList(C5, Eb5, G5)))) {
                code.add(ChordName.EVAL);
            } else if (Objects.equals(chord, new ArrayList<>(Arrays.asList(Db5, E5, Ab5)))) {
                code.add(ChordName.JUMP);
            } else if (Objects.equals(chord, new ArrayList<>(Arrays.asList(D5, F5, A5)))) {
                code.add(ChordName.JUMP_IF);
            } else if (Objects.equals(chord, new ArrayList<>(Arrays.asList(Eb5, Gb5, Bb5)))) {
                code.add(ChordName.CURRENT_VALUE);
            } else if (chord.size() == 1) {
                code.add((float)chord.getFirst().ordinal());
            } else {
                throw new IllegalArgumentException("Unknown Chord: " + chord + " at index: " + index);
            }
            index++;
        }
    }

    public void run() throws IOException {
        if (debug) {
            System.out.println(code);
        }
        if (!code.getFirst().equals(ChordName.START) || !code.get(1).equals(ChordName.START2)) {
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
            float[] arguments = getArguments(index);
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
