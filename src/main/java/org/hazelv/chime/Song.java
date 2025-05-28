package org.hazelv.chime;
import java.io.IOException;
import java.util.*;

import static org.hazelv.chime.NoteName.*;

public class Song {
    public List<List<NoteName>> chords;
    public List<Object> code = new ArrayList<>();
    public List<Stack<Float>> data = new ArrayList<>();
    public float currentValue = 0.0f;

    public Song(List<List<NoteName>> chords) {
        this.chords = chords;
        for (int i = 0; i < 8; i++) {
            data.add(new Stack<>());
        }
    }

    public void parse() {
        for (List<NoteName> chord : chords) {
            if (Objects.equals(chord, new ArrayList<>(Arrays.asList(C5, E5, G5, C3, E3, G3)))) {
                code.add(ChordName.START);
            } else if (Objects.equals(chord, new ArrayList<>(Arrays.asList(C7, E7, G7, C3, Eb3, G3)))) {
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
                code.add(ChordName.STORE);
            } else if (Objects.equals(chord, new ArrayList<>(Arrays.asList(Bb5, D6, F6)))) {
                code.add(ChordName.GET);
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
            }
        }
    }

    public void run() throws IOException {
        if (!code.getFirst().equals(ChordName.START) || !code.get(1).equals(ChordName.START2)) {
            System.out.println(chords);
            throw new IllegalArgumentException("Invalid start sequence. (Are you sure this is a program?)");
        }
        //System.out.println(code);
        for (int i = 0; i < (code.size() - 1); i++) {
            Object instruction = code.get(i);
            if (instruction.equals(ChordName.START) || instruction.equals(ChordName.START2)) {
                if (i > 1) {
                    throw new IllegalArgumentException("Start sequence repeated?");
                }
                i++;
                continue;
            } else if (instruction instanceof Integer) {
                throw new IllegalArgumentException("Integer literal before instruction at chord: " + i);
            }
            //System.out.println(code + ": " + i);
            float[] arguments = getArguments(i);
            switch (instruction) {
                case ChordName.ADD:
                    currentValue = arguments[0] + arguments[1];
                    System.out.println(currentValue);
                    i = i + 2;
                    break;
                case ChordName.SUBTRACT:
                    currentValue = arguments[0] - arguments[1];
                    i = i + 2;
                    break;
                case ChordName.MULTIPLY:
                    currentValue = arguments[0] * arguments[1];
                    i = i + 2;
                    break;
                case ChordName.DIVIDE:
                    currentValue = arguments[0] / arguments[1];
                    i = i + 2;
                    break;
                case ChordName.PRINT:
                    System.out.print(currentValue);
                    break;
                case ChordName.STORE:
                    if (data.size() < arguments[0]) {
                        for (int ii = 0; ii < arguments[0]; ii++) {
                            data.add(new Stack<>());
                        }
                    }
                    data.get((int)arguments[0]).push(currentValue);
                    i++;
                    break;
                case ChordName.GET:
                    currentValue = data.get((int)arguments[0]).pop();
                    i++;
                    break;
                case ChordName.INPUT:
                    currentValue = (float)System.in.read();
                    break;
                case ChordName.HOLD:
                    currentValue = arguments[0];
                    i++;
                    break;
                case ChordName.PRINT_CHAR:
                    System.out.print((char)currentValue);
                    break;
                case ChordName.PRINTLN:
                    System.out.println();
                    break;
                case ChordName.EVAL: // determine relationship between currentValue and first argument, ERASES currentValue
                    if (currentValue < arguments[0]) {
                        currentValue = 1;
                    } else if (currentValue == arguments[0]) {
                        currentValue = 2;
                    } else if (currentValue > arguments[0]) {
                        currentValue = 3;
                    }
                    i++;
                    break;
                case ChordName.JUMP: // chord count starts at 0
                    i = (int)(arguments[0] - 1); // subtract one as for loop will add one at the end
                    break;
                case ChordName.JUMP_IF: // first argument is jump chord, second argument is wanted condition from eval or otherwise
                    if (currentValue == arguments[1]) {
                        i = (int)(arguments[0] - 1);
                    }
                    break;
                default:
                    throw new IllegalStateException("Unknown Instruction: " + instruction + " at chord: " + i);
            }
        }

    }

    @Override
    public String toString() {
        return chords.toString() + " -> " + code.toString();
    }

    public float[] getArguments(int index) {
        if (code.get(index + 1).equals(ChordName.CURRENT_VALUE)) {
            return new float[]{currentValue, (float)code.get(index + 2)};
        } else if (code.get(index + 2).equals(ChordName.CURRENT_VALUE)) {
            return new float[]{(float)code.get(index + 1), currentValue};
        } else if (code.get(index + 1).equals(ChordName.CURRENT_VALUE) && code.get(index + 2).equals(ChordName.CURRENT_VALUE)) {
            return new float[]{currentValue, currentValue};
        } else {
            return new float[]{(float)code.get(index + 1), (float)code.get(index + 2)};
        }
    }
}
