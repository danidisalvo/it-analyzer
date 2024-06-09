package com.probendi.itanalyzer;

import java.util.List;

/**
 * An element parsed from a JSON file.
 *
 * @param work       the work
 * @param position   the position within the work
 * @param text       the text
 * @param relevant   {@code true} if this element is relevant
 * @param opposition {@code true} if this element is an opposition
 * @param senses     the senses of being
 *                   <p>
 *                   Copyright &copy; 2024, Daniele Di Salvo
 * @author Daniele Di Salvo
 * @since 1.0
 */
public record JsonElement(String work,
                          String position,
                          String text,
                          boolean relevant,
                          boolean opposition,
                          List<String> senses) {
}
