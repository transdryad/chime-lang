A primitive interpreted language from midi files (known to fully support MuseScore midi export).

Language Reference: (note that all sharps are referred to by their enharmonic equivalent - so instead of C#5, Db5)

Chords are considered to be any notes that begin at the exact same time, not notes that are playing together eventually.
Chords can be spread across different tracks and instruments. Duplicate notes in a chord are ignored.

For a file to be considered a chime program, it must contain a primitive 'header' with the following chords:
C5, E5, G5, C3, E3, G3, followed by C7, E7, G7, C3, Eb3, G3. No other notes may play during the header.

There is a variable/accumulator (currentVal) that often stores the result of instructions.

Instructions in chime have two arguments or fewer. Arguments are the chords directly following an instruction.
To pass an argument literal, add a single note chord (or two) after the instruction. The value of the argument is derived from the midi key number.
For a reference on wha those key numbers mean in terms of note in a composition program, visit either the NoteName enum class or [this reference](https://inspiredacoustics.com/en/MIDI_note_numbers_and_center_frequencies).

Chords have their root in the 5th octave, except those in the file 'header'.

The tested column indicates whether a test has been written for a given feature.

| Name        | Chord Name        | Chord                   |  'Args'  | Tested | Action/Function                                                                 | Notes                                                   |
|:------------|:------------------|:------------------------|:--------:|:------:|:--------------------------------------------------------------------------------|---------------------------------------------------------|
| START       | C major x2        | C5, E5, G5, C3, E3, G3  |    0     |   x    | First part of a chime file 'header'.                                            |                                                         |
| START2      | C major + C minor | C7, E7, G7, C3, Eb3, G3 |    0     |   x    | Second part of the file 'header'.                                               |                                                         |
| ADD         | D major           | D5, Gb5, A5             |    2     |   x    | Add two arguments and store in currentVal.                                      |                                                         |
| SUBTRACT    | E major           | E5, Ab5, B5             |    2     |   x    | Subtract second argument from first argument and store in currentVal.           |                                                         |
| MULTIPLY    | F major           | F5, A5, C6              |    2     |   x    | Multiply arguments and store in currentVal.                                     |                                                         |
| DIVIDE      | G major           | G5, B5, D6              |    2     |   x    | Divide first argument by second argument and store in currentVal                |                                                         |
| PRINT       | A major           | A5, Db6, E6             |    0     |   x    | Print currentVal to standard output.                                            |                                                         |
| PUSH        | B major           | B5, Eb6, Gb6            |    1     |   x    | Push currentVal to the stack of the first argument.                             |                                                         |
| POP         | Bb major          | Bb5, D6, F6             |    1     |   x    | Pop from the stack given in the first argument and store in currentVal.         |                                                         |
| INPUT       | Db major          | Db5, F5, Ab5            |    0     |   x    | Read the next byte of data from standard input and store in currentVal.         |                                                         |
| HOLD        | Eb major          | Eb5, G5, Bb5            |    1     |   x    | Put the first argument in currentVal.                                           |                                                         |
| PRINT_CHAR  | Gb major          | Gb5, Bb5, Db6           |    0     |   x    | Print currentVal standard output as a char.                                     | Print ascii chars/unicode chars up to 128/0x80.         |
| PRINTLN     | Ab major          | Ab5, C6, Eb6            |    0     |   x    | Print a newline to standard output.                                             |                                                         |
| EVAL        | C minor           | C5, Eb5, G5             |    2     |   x    | Determine relationship between arguments. Stores result in currentVal.          | 1 - arg1 < arg2, 2 - arg1 == arg2, 3 - arg1 > arg2      |
| JUMP        | Db minor          | Db5, E5, Ab5            |    1     |   x    | Jump to the chord index from the first argument.                                | Indices start at 0 for the first START in the 'header'. |
| JUMP_IF     | D minor           | D5, F5, A5              |    2     |   x    | Jump to the first argument index if currentVal is equal to the second argument. | ^                                                       |
| CURRENT_VAL | Eb minor          | Eb5, Gb5, Bb5           |   N/A    |   x    | Refer to currentVal to pass it as an argument.                                  |                                                         | 

copied code from song parse method that is being changed, but I need the reference:
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

run func old
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