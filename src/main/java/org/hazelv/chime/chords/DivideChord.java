// Copyright 2025 Hazel Viswanath <viswanath.hazel@gmail.com>.

// This file is part of Chime-Lang.

// Chime-Lang is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
// License as published by the Free Software Foundation, either version 3 of the License, or (at your option)
// any later version.

// Chime-Lang is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
// the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
// See LICENSE in the project root for more details.

// You should have received a copy of the GNU General Public License along with Chime-Lang. If not, see <https://www.gnu.org/licenses/>.

package org.hazelv.chime.chords;

import org.hazelv.chime.Chord;

import static org.hazelv.chime.Main.song;

public class DivideChord implements Chord {
    @Override
    public void execute() {
        song.currentValue = song.arguments[0] / song.arguments[1];
    }

    @Override
    public void increment() {
        song.index = song.index + 3;
    }
}
