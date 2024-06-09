package com.probendi.itanalyzer;

import java.io.IOException;
import java.util.List;

/**
 * Reads senses of being and oppositions from a file.
 * <p>
 * Copyright &copy; 2024, Daniele Di Salvo
 *
 * @author Daniele Di Salvo
 * @since 1.0
 */
public interface Reader {

    /**
     * Returns the read senses of being.
     *
     * @return the read senses of being
     */
    List<SenseOfBeing> getSensesOfBeing();

    /**
     * Returns the read oppositions.
     *
     * @return the read oppositions
     */
    List<Opposition> getOppositions();

    /**
     * Reads entries from a file.
     *
     * @param file the file's name
     * @throws IllegalArgumentException if file is {@code null} or empty
     * @throws IOException              if an I/O error occurs
     */
    void read(String file) throws IOException;

    /**
     * Validates the given file and entries.
     *
     * @param file the file's name
     * @return the trimmed file's name
     * @throws IllegalArgumentException if file is {@code null} or empty
     */
    default String validate(String file) {
        if (file == null) {
            throw new IllegalArgumentException("file cannot be null");
        }
        if (file.isEmpty()) {
            throw new IllegalArgumentException("file cannot be blank");
        }
        return file.trim();
    }
}
