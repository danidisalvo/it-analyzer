package com.probendi.itanalyzer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Reads a tab-separated CSV file or a JSON file with the columns:
 * <ul>
 *     <li>work</li>
 *     <li>position</li>
 *     <li>text</li>
 *     <li>relevant</li>
 *     <li>opposition</li>
 *     <li>senses</li>
 * </ul>
 * it-analyzer analyzes the file and saves its results in the following files:
 * <ul>
 *     <li>senses.txt, containing the number of times a sense of being appears in Aquinas's corpus</li>
 *     <li>oppositions.txt, containing the number of times an opposition appears in Aquinas's corpus</li>
 *     <li>senses-per-work.txt, containing the number of senses of being per work</li>
 *     <li>oppositions-per-work.txt, containing the number of oppositions per work</li>
 *     <li>positions-per-sense.txt, containing the positions where a sense of being appears in Aquinas's corpus</li>
 *     <li>positions-per-opposition.txt, containing the positions where an opposition appears in Aquinas's corpus</li>
 * </ul>
 * <p>
 * Copyright &copy; 2024, Daniele Di Salvo
 *
 * @author Daniele Di Salvo
 * @since 1.0
 */
public class ITAnalyzer {

    private static final String OPPOSITIONS_TXT = "oppositions.txt";
    private static final String SENSES_TXT = "senses.txt";

    private static final String OPPOSITIONS_PER_WORK_TXT = "oppositions-per-work.txt";
    private static final String SENSES_PER_WORK_TXT = "senses-per-work.txt";

    private static final String POSITIONS_PER_OPPOSITION_TXT = "positions-per-opposition.txt";
    private static final String POSITIONS_PER_SENSE_TXT = "positions-per-sense.txt";

    private static final String CSV = ".csv";
    private static final String JSON = ".json";
    private static final String TXT = ".txt";

    private static final String USAGE = "Usage: java -jar it-analyzer-1.0.0.jar file";

    /**
     * Runs {@code it-analyzer}.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println(USAGE);
            System.exit(-1);
        }

        if (!args[0].endsWith(CSV) && !args[0].endsWith(JSON) && !args[0].endsWith(TXT)) {
            System.err.println("Unsupported file type: must be csv, txt, or json");
            System.exit(-1);
        }

        try {
            ITAnalyzer analyzer = new ITAnalyzer();
            analyzer.analyze(args[0]);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Analyzes the given file.
     *
     * @param file the file to be analyzed
     * @throws IOException if an error occurs
     */
    public void analyze(String file) throws IOException {
        Reader reader = file.endsWith(JSON) ? new JsonReader() : new CsvReader();
        reader.read(file);

        Map<String, Integer> sensesOfBeing = new TreeMap<>();
        Map<String, Integer> sensesOfBeingPerWork = new TreeMap<>();
        Map<String, Set<String>> sensesOfBeingPositions = new TreeMap<>();

        reader.getSensesOfBeing().forEach(t -> {
            Integer count = sensesOfBeing.get(t.text());
            count = count == null ? 1 : count + 1;
            sensesOfBeing.put(t.text(), count);

            count = sensesOfBeingPerWork.get(t.work());
            count = count == null ? 1 : count + 1;
            sensesOfBeingPerWork.put(t.work(), count);

            Set<String> positions = sensesOfBeingPositions.get(t.text());
            if (positions == null) {
                positions = new TreeSet<>();
            }
            positions.add(t.position());
            sensesOfBeingPositions.put(t.text(), positions);
        });

        Map<String, Integer> oppositions = new TreeMap<>();
        Map<String, Integer> oppositionsPerWork = new TreeMap<>();
        Map<String, Set<String>> oppositionsPositions = new TreeMap<>();

        reader.getOppositions().forEach(t -> {
            Integer count = oppositions.get(t.values().toString());
            count = count == null ? 1 : count + 1;
            oppositions.put(t.values().toString(), count);

            count = oppositionsPerWork.get(t.work());
            count = count == null ? 1 : count + 1;
            oppositionsPerWork.put(t.work(), count);

            Set<String> positions = oppositionsPositions.get(t.values().toString());
            if (positions == null) {
                positions = new TreeSet<>();
            }
            positions.add(t.position());
            oppositionsPositions.put(t.values().toString(), positions);
        });

        printOccurrences(oppositions, OPPOSITIONS_TXT);
        printOccurrences(sensesOfBeing, SENSES_TXT);

        printOrderedOccurrences(oppositionsPerWork, OPPOSITIONS_PER_WORK_TXT);
        printOrderedOccurrences(sensesOfBeingPerWork, SENSES_PER_WORK_TXT);

        printDistributions(oppositionsPositions, POSITIONS_PER_OPPOSITION_TXT);
        printDistributions(sensesOfBeingPositions, POSITIONS_PER_SENSE_TXT);
    }

    /**
     * Prints the occurrences of an opposition or a sense of being.
     *
     * @param map  the map containing the occurrences
     * @param file the output file's name
     */
    private void printOccurrences(Map<String, Integer> map, String file) {
        try (final FileWriter fw = new FileWriter(file); final BufferedWriter bw = new BufferedWriter(fw)) {
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                String key = entry.getKey().replace("[", "").replace("]", "");
                bw.write(String.format("%s: %d\n", key, entry.getValue()));
            }
        } catch (IOException e) {
            System.err.printf("failed to write %s [%s]\n", file, e.getMessage());
        }
    }

    /**
     * Prints the reverse-ordered occurrences of an opposition or a sense of being.
     *
     * @param map  the map containing the occurrences
     * @param file the output file's name
     */
    private void printOrderedOccurrences(Map<String, Integer> map, String file) {
        try (final FileWriter fw = new FileWriter(file); final BufferedWriter bw = new BufferedWriter(fw)) {
            map.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).forEach(t -> {
                String key = t.getKey().replace("[", "").replace("]", "");
                try {
                    bw.write(String.format("%s: %d\n", key, t.getValue()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            System.err.printf("failed to write %s [%s]\n", file, e.getMessage());
        }
    }

    /**
     * Prints the distributions of an opposition or a sense of being.
     *
     * @param map  the map containing the occurrences
     * @param file the output file's name
     */
    private void printDistributions(Map<String, Set<String>> map, String file) {
        try (final FileWriter fw = new FileWriter(file); final BufferedWriter bw = new BufferedWriter(fw)) {
            for (Map.Entry<String, Set<String>> entry : map.entrySet()) {
                String key = entry.getKey().replace("[", "").replace("]", "");
                bw.write(String.format("%s:\n", key));
                for (String t : entry.getValue()) {
                    bw.write(String.format("\t%s\n", t));
                }
            }
        } catch (IOException e) {
            System.err.printf("failed to write %s [%s]\n", file, e.getMessage());
        }
    }
}
