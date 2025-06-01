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

