A primitive interpreted language from midi files (known to fully support MuseScore midi export).

Language Reference: (note that all sharps are referred to by their flat enharmonic - so instead of C#5, Db5)

Chords are considered to be any notes that begin at the exact same time, not notes that are playing together eventually.
For a file to be considered a chime program, it must contain a primitive 'header' with the following chords:
C5, E5, G5, C3, E3, G3, followed by C7, E7, G7, C3, Eb3, G3. No other notes may play during the header. There is a 'shadow'
variable (currentVal) that can't be directly referenced but is very useful.

Instructions in chime have two arguments or fewer. Arguments are the chords directly following an instruction.

| Name      | Chord Name        | Chord                   | Explanation/Action                            |
|:----------|:------------------|-------------------------|-----------------------------------------------|
| START     | C major x2        | C5, E5, G5, C3, E3, G3  | First part of a chime file 'header'           |
| START2    | C major + C minor | C7, E7, G7, C3, Eb3, G3 | Second part of the file 'header'              |
| ADD       | D major           | D5, Gb5, A5             | Add the two arguments and store in currentVal |
| SUBTRACT  | 