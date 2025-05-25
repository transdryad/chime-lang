package org.hazelv.chime;
import java.util.*;

import static org.hazelv.chime.NoteName.*;

public class Song {
    public List<List<NoteName>> chords;
    public List<Object> code = new ArrayList<>();
    public List<Stack<Float>> data = new ArrayList<>();
    public float currentValue = 0;

    public Song(List<List<NoteName>> chords) {
        this.chords = chords;
        for (int i = 0; i < 128; i++) {
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
            } else if (chord.size() == 1) {
                code.add(chord.getFirst().ordinal());
            }
        }
    }

    public void run() {
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
                    System.out.println(currentValue);
                    break;
                case ChordName.STORE:
                    data.get((int) arguments[0]).push(arguments[0]);
                    i++;
                    break;
                case ChordName.GET:
                    currentValue = data.get((int) arguments[0]).pop();
                    i++;
                    break;
                default:
                    throw new IllegalStateException("Unknown Instruction: " + instruction + " at chord: " + i);
            }
        }

    }

    @Override
    public String toString() {
        return chords.toString();
    }

    public float[] getArguments(int index) {
        return new float[]{(float)code.get(index + 1), (float)code.get(index + 2)};
    }
}
