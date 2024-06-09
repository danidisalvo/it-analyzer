package com.probendi.itanalyzer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Reads entries from a JSON file.
 * <p>
 * Copyright &copy; 2024, Daniele Di Salvo
 *
 * @author Daniele Di Salvo
 * @since 1.0
 */
public class JsonReader implements Reader {

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
     * @param file the file's name
     * @throws IllegalArgumentException if file is {@code null} or empty
     * @throws IOException              if an I/O error occurs
     */
    public void read(String file) throws IOException {
        Path path = Paths.get(file);
        ObjectMapper mapper = new ObjectMapper();

        TypeReference<List<JsonElement>> typeRef = new TypeReference<>() {
        };
        mapper.readValue(path.toFile(), typeRef).forEach(t -> {
            if (t.relevant()) {
                if (t.opposition()) {
                    Collections.sort(t.senses());
                    oppositions.add(new Opposition(t.work(), t.position(), t.senses()));
                }
                t.senses().forEach(v -> sensesOfBeings.add(new SenseOfBeing(t.work(), t.position(), v)));
            }
        });
    }
}
