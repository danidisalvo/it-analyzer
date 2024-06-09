package com.probendi.itanalyzer;

/**
 * A sense of being parsed from a CSV or a JSON file.
 *
 * @param work     the work
 * @param position the position within the work
 * @param text     the sense of being
 *                 <p>
 *                 Copyright &copy; 2024, Daniele Di Salvo
 * @author Daniele Di Salvo
 * @since 1.0
 */
public record SenseOfBeing(String work, String position, String text) {
}
