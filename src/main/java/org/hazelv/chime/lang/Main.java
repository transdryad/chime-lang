// Copyright 2025 Hazel Viswanath <viswanath.hazel@gmail.com>.

// This file is part of Chime-Lang.

// Chime-Lang is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
// License as published by the Free Software Foundation, either version 3 of the License, or (at your option)
// any later version.

// Chime-Lang is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
// the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
// See LICENSE in the project root for more details.

// You should have received a copy of the GNU General Public License along with Chime-Lang. If not, see <https://www.gnu.org/licenses/>.

package org.hazelv.chime.lang;
import org.hazelv.chime.lang.chords.*;
import org.tomlj.Toml;
import org.tomlj.TomlArray;
import org.tomlj.TomlParseResult;

import javax.sound.midi.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;
import java.util.jar.*;
import java.util.stream.Collectors;

import static org.hazelv.chime.lang.NoteName.*;

public class Main {
    public static List<NoteEvent> noteEvents;
    public static HashMap<List<NoteName>, Class<?>> chordMap;
    public static List<Chord> chords;
    public static int bpm;
    public static Song song;
    public static boolean debug = false;

    public static void main(String[] args) {
        noteEvents = new ArrayList<>();
        chords = new ArrayList<>();
        chordMap = new HashMap<>();
        registerDefaultChords();
        if (args.length < 1) {throw new IllegalStateException("A filename must be provided");}
        if (args.length >= 2) {
            if (Objects.equals(args[1], "--debug")) {
                debug = true;
            }
        }
        try {
            String configPath = System.getenv("CHIME_CONFIG");
            if (configPath == null || configPath.isEmpty()) {
                configPath = "config.toml";
            }
            File configFile = new File(configPath);
            if (configFile.exists()) {
                TomlParseResult result = Toml.parse(configFile.toPath());
                result.errors().forEach(error -> System.err.println(error.toString()));
                TomlArray resultArray = result.getArray("Libraries.used");
                loadLibraries(resultArray);
            }
            File file = new File(args[0]);
            //File file = new File("test/Test2.mid");
            Sequence sequence = MidiSystem.getSequence(file);
            long res = sequence.getTickLength();
            getMidiEvents(sequence);
            noteEvents.sort(Comparator.comparingInt(NoteEvent::getTimestamp));
            bpm = noteEvents.getFirst().bpm;
            //System.out.println("bpm: "+bpm);
            getChords(res);
            song = songFromChords(chords);
            song.run();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            if (debug) {
                System.err.println("Error: " + e.getClass() + ": " + e.getMessage());
                System.err.println(Arrays.toString(e.getStackTrace()));
            }
        }
    }

    public static Song songFromChords(List<Chord> chords) {
        return new Song(chords, debug);
    }

    public static void registerDefaultChords() {
        registerChord(new ArrayList<>(Arrays.asList(C3, E3, G3, C5, E5, G5)), StartChord.class);
        registerChord(new ArrayList<>(Arrays.asList(C3, Eb3, G3, C7, E7, G7)), Start2Chord.class);
        registerChord(new ArrayList<>(Arrays.asList(D5, Gb5, A5)), AddChord.class);
        registerChord(new ArrayList<>(Arrays.asList(E5, Ab5, B5)), SubtractChord.class);
        registerChord(new ArrayList<>(Arrays.asList(F5, A5, C6)), MultiplyChord.class);
        registerChord(new ArrayList<>(Arrays.asList(G5, B5, D6)), DivideChord.class);
        registerChord(new ArrayList<>(Arrays.asList(A5, Db6, E6)), PrintChord.class);
        registerChord(new ArrayList<>(Arrays.asList(B5, Eb6, Gb6)), PushChord.class);
        registerChord(new ArrayList<>(Arrays.asList(Bb5, D6, F6)), PopChord.class);
        registerChord(new ArrayList<>(Arrays.asList(Db5, F5, Ab5)), InputChord.class);
        registerChord(new ArrayList<>(Arrays.asList(Eb5, G5, Bb5)), HoldChord.class);
        registerChord(new ArrayList<>(Arrays.asList(Gb5, Bb5, Db6)), PrintCharChord.class);
        registerChord(new ArrayList<>(Arrays.asList(Ab5, C6, Eb6)), PrintLnChord.class);
        registerChord(new ArrayList<>(Arrays.asList(C5, Eb5, G5)), EvalChord.class);
        registerChord(new ArrayList<>(Arrays.asList(Db5, E5, Ab5)), JumpChord.class);
        registerChord(new ArrayList<>(Arrays.asList(D5, F5, A5)), JumpIfChord.class);
        registerChord(new ArrayList<>(Arrays.asList(Eb5, Gb5, Bb5)), CurrentValChord.class);
    }

    public static void registerChord(List<NoteName> notes, Class<?> chordType) {
        chordMap.put(notes, chordType);
    }

    public static void getMidiEvents(Sequence sequence) {
        for (Track track : sequence.getTracks()) {
            for (int i = 0; i < track.size(); i++) {
                MidiEvent event = track.get(i);
                MidiMessage message = event.getMessage();
                NoteEvent note = new NoteEvent(event, message);
                if (note.action != -1) {
                    noteEvents.add(note);
                }
            }
        }
    }

    public static void getChords(long res) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        for (int i = 0; i < res; i++) {
            int finalI = i;
            List<NoteEvent> timedEvents = noteEvents.stream().filter(item -> item.getTimestamp() == finalI && item.action != 0).toList();
            if (!timedEvents.isEmpty()) {
                List<NoteName> notes = new ArrayList<>();
                for (NoteEvent event : timedEvents) {
                    notes.add(NoteName.values()[event.getNoteNumber()]);
                }
                notes= notes.stream().distinct().collect(Collectors.toList());
                notes.sort(Comparator.comparingInt(NoteName::ordinal));
                if (chordMap.containsKey(notes)) {
                    chords.add((Chord)chordMap.get(notes).getDeclaredConstructor().newInstance());
                } else if (notes.size() == 1) {
                    chords.add(new LiteralChord(notes.getFirst().ordinal()));
                } else {
                    throw new IllegalArgumentException("Unknown Chord: " + notes + " at index: " + i);
                }
            }
        }
    }

    public static void loadLibraries(TomlArray resultArray) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        URL[] urls = new URL[resultArray.size()];
        List<String> classNames = new ArrayList<>();
        List<String> mains = new ArrayList<>();
        for (int i = 0; i < resultArray.size(); i++) {
            String fileName = resultArray.getString(i);
            JarFile libraryFile = new JarFile(fileName);
            FileInputStream fis = new FileInputStream(fileName);
            JarInputStream jarStream = new JarInputStream(fis);
            Manifest manifest = jarStream.getManifest();
            if (manifest != null) {
                Attributes attributes = manifest.getMainAttributes();
                String mc = attributes.getValue("Main-Class");
                mains.add(mc);
            }
            jarStream.close();
            fis.close();
            URL url = new File(resultArray.getString(i)).toURI().toURL();
            urls[i] = url;
            Enumeration<JarEntry> e = libraryFile.entries();
            while (e.hasMoreElements()) {
                JarEntry je = e.nextElement();
                if(je.isDirectory() || !je.getName().endsWith(".class")){
                    continue;
                }
                String className = je.getName().substring(0,je.getName().length()-".class".length());
                className = className.replace('/', '.');
                classNames.add(className);
            }
        }
        DynamicClassLoader cl = new DynamicClassLoader(urls, ClassLoader.getSystemClassLoader());
        for (String className : classNames) {
            cl.loadClass(className);
        }
        for (String main : mains) {
            Class<?> c = Class.forName(main);
            c.getMethod("main", String[].class).invoke(null, (Object) new String[]{});
        }
    }
}