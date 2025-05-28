A primitive interpreted language from midi files (known to fully support MuseScore midi export).

Language Reference: (note that all sharps are referred to by their enharmonic equivalent - so instead of C#5, Db5)

Chords are considered to be any notes that begin at the exact same time, not notes that are playing together eventually.
For a file to be considered a chime program, it must contain a primitive 'header' with the following chords:
C5, E5, G5, C3, E3, G3, followed by C7, E7, G7, C3, Eb3, G3. No other notes may play during the header.

There is a variable/accumulator (currentVal) that often stores the result of instructions.

Instructions in chime have two arguments or fewer. Arguments are the chords directly following an instruction.
Most chords have their root in the 5 octave, except those in the file 'header'.

| Name        | Chord Name        | Chord                   | Action/Function                                                                                                                                                                    |
|:------------|:------------------|:------------------------|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| START       | C major x2        | C5, E5, G5, C3, E3, G3  | First part of a chime file 'header'.                                                                                                                                               |
| START2      | C major + C minor | C7, E7, G7, C3, Eb3, G3 | Second part of the file 'header'.                                                                                                                                                  |
| ADD         | D major           | D5, Gb5, A5             | Add two arguments and store in currentVal.                                                                                                                                         |
| SUBTRACT    | E major           | E5, Ab5, B5             | Subtract second argument from first argument and store in currentVal.                                                                                                              |
| MULTIPLY    | F major           | F5, A5, C6              | Multiply arguments and store in currentVal.                                                                                                                                        |
| DIVIDE      | G major           | G5, B5, D6              | Divide first argument by second argument and store in currentVal                                                                                                                   |
| PRINT       | A major           | A5, Db6, E6             | Print currentVal to standard output.                                                                                                                                               |
| STORE       | B major           | B5, Eb6, Gb6            | Push currentVal to the  stack of the second argument (int).                                                                                                                        |
| GET         | Bb major          | Bb5, D6, F6             | Pop from the stack given in the first argument (int) and store in currentVal.                                                                                                      |
| INPUT       | Db major          | Db5, F5, Ab5            | Read the next byte of data from standard input and store in currentVal.                                                                                                            |
| HOLD        | Eb major          | Eb5, G5, Bb5            | Put the first argument in currentVal.                                                                                                                                              |
| PRINT_CHAR  | Gb major          | Gb5, Bb5, Db6           | Print currentVal standard output as a char (print ascii chars/unicode chars up to 128/0x80)                                                                                        |
| PRINTLN     | Ab major          | Ab5, C6, Eb6            | Print a newline to standard output.                                                                                                                                                |
| EVAL        | C minor           | C5, Eb5, G5             | Determine relationship between currentVal and first argument (1 - currentValue < argument, 2 - currentValue == argument, 3 - currentValue > argument). Stores result in currentVal |
| JUMP        | Db minor          | Db5, E5, Ab5            | Jump to the chord index from the first argument, indices start at 0 for the first START in the 'header'.                                                                           |
| JUMP_IF     | D minor           | D5, F5, A5              | Jump to the first argument index if currentVal is equal to the second argument.                                                                                                    |
| CURRENT_VAL | Eb minor          | Eb5, Gb5, Bb5           | Refer to currentVal, likely to pass it as an argument.                                                                                                                             | 