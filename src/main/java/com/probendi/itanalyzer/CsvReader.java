package com.probendi.itanalyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Reads entries from a tab-separated CSV file.
 *
 * Copyright &copy; 2024, Daniele Di Salvo
 *
 * @author Daniele Di Salvo
 * @since 1.0
 */
public class CsvReader implements Reader {

    private static final String COMMA = ",";
    private static final String TRUE = "true";

    private final List<SenseOfBeing> sensesOfBeings = new LinkedList<>();
    private final List<Opposition> oppositions = new LinkedList<>();

    @Override
    public List<SenseOfBeing> getSensesOfBeing() {
        return sensesOfBeings;
    }

    @Override
    public List<Opposition> getOppositions() {
        return oppositions;
    }

    /**
     * Reads entries from a file.
     *
     * @param file    the file's name
     * @throws IllegalArgumentException if file is {@code null} or empty
     * @throws IOException              if an I/O error occurs
     */
    public void read(String file) throws IOException {
        String name = validate(file);
        Path path = Paths.get(file);
        try (BufferedReader br = Files.newBufferedReader(path)) {
            for(String line; (line = br.readLine()) != null; ) {
                String[] tokens = line.trim().split("\t");
                if (tokens.length >= 4 && tokens.length <= 6) {
                    String work = tokens[0];
                    String position = tokens[1];
                    if (TRUE.equalsIgnoreCase(tokens[3])) {
                        List<String> values = new LinkedList<>();
                        Arrays.stream(tokens[5].split(COMMA)).sequential().forEach(t -> values.add(t.trim()));
                        if (TRUE.equalsIgnoreCase(tokens[4])) {
                            Collections.sort(values);
                            oppositions.add(new Opposition(work, position, values));
                        }
                        values.forEach(v -> sensesOfBeings.add(new SenseOfBeing(work, position, v)));
                    }
                }
            }
        }
        System.out.printf("Read %d senses of being from %s\n", sensesOfBeings.size(), name);
        System.out.printf("Read %d oppositions from %s\n", oppositions.size(), name);
    }
}
