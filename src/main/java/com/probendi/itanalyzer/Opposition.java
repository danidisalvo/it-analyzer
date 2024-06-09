package com.probendi.itanalyzer;

import java.util.List;

/**
 * An opposition parsed from a CSV or a JSON file.
 *
 * @param work     the work
 * @param position the position within the work
 * @param values   the values that constitute this opposition
 *                 <p>
 *                 Copyright &copy; 2024, Daniele Di Salvo
 * @author Daniele Di Salvo
 * @since 1.0
 */
public record Opposition(String work, String position, List<String> values) {
}
