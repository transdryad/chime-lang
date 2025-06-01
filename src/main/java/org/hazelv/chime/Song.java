// Copyright 2025 Hazel Viswanath <viswanath.hazel@gmail.com>.

// This file is part of Chime-Lang.

// Chime-Lang is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
// License as published by the Free Software Foundation, either version 3 of the License, or (at your option)
// any later version.

// Chime-Lang is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
// the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
// See LICENSE in the project root for more details.

// You should have received a copy of the GNU General Public License along with Chime-Lang. If not, see <https://www.gnu.org/licenses/>.

package org.hazelv.chime;
import org.hazelv.chime.chords.*;

import java.io.IOException;
import java.util.*;

public class Song {
    public List<Chord> chords;
    public List<Stack<Float>> data;
    public float currentValue = 0.0f;
    public boolean debug;
    public int index = 0;
    public float[] arguments;

    public Song(List<Chord> chords, boolean debug) {
        this.chords = chords;
        this.data = new ArrayList<>();
        this.debug = debug;
        for (int i = 0; i < 2; i++) {
            data.add(new Stack<>());
        }
    }

    public void run() throws IOException {
        if (debug) {
            for (Chord chord : chords) {
                if (chord instanceof LiteralChord) {
                    System.out.print(((LiteralChord) chord).value + ", ");
                } else {
                    System.out.print(chord.getClass().getName().substring(chord.getClass().getName().lastIndexOf(".") + 1) + ", ");
                }
            }
        }
        if (!(chords.getFirst() instanceof StartChord) || !(chords.get(1) instanceof Start2Chord)) {
            throw new IllegalArgumentException("Invalid 'header' for chime file. (Are you sure this is a program?)");
        }
        while (index < chords.size()) {
            Chord instruction = chords.get(index);
            arguments = getArguments(index);
            if (instruction instanceof StartChord || instruction instanceof Start2Chord) {
                if (index > 1) {
                    throw new IllegalArgumentException("Start sequence repeated?");
                }
            } else if (instruction instanceof LiteralChord) {
                throw new IllegalArgumentException("Literal before instruction at chord: " + index + ". Bad jump?");
            }
            instruction.execute();
            instruction.increment();
        }
    }

    @Override
    public String toString() {
        return chords.toString();
    }

    public float[] getArguments(int index) {
        if (index == (chords.size() - 1)) { // end of list
            return new float[]{};
        } else if (index == (chords.size() - 2)) { // one arg until end
            if (chords.get(index + 1) instanceof CurrentValChord) {
                return new float[]{currentValue};
            } else {
                return new float[]{getArgument(index + 1)};
            }
        } else if (index <= (chords.size() - 3)) {
            if (chords.get(index + 1) instanceof CurrentValChord) {
                return new float[]{currentValue, getArgument(index + 2)};
            } else if (chords.get(index + 2) instanceof CurrentValChord) {
                return new float[]{getArgument(index + 1), currentValue};
            } else if (chords.get(index + 1) instanceof CurrentValChord && chords.get(index + 2) instanceof CurrentValChord) {
                return new float[]{currentValue, currentValue};
            } else {
                return new float[]{getArgument(index + 1), getArgument(index + 2)};
            }
        } else {
            return new float[]{};
        }
    }

    public float getArgument(int index) {
        if (!(chords.get(index) instanceof LiteralChord)) {
            return Float.NaN;
        } else {
            return ((LiteralChord) chords.get(index)).value;
        }
    }
}
