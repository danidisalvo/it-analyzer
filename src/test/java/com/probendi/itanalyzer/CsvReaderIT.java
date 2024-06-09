package com.probendi.itanalyzer;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CsvReaderIT {

    @Test
    void read() throws IOException {
        List<Opposition> oppositions = new LinkedList<>();
        oppositions.add(new Opposition("work 4", "position 1", List.of("opposition a", "opposition b")));
        oppositions.add(new Opposition("work 5", "position 1", List.of("opposition c", "opposition d", "opposition e")));

        List<SenseOfBeing> senseOfBeings = new LinkedList<>();
        senseOfBeings.add(new SenseOfBeing("work 1", "position 1", "sense 1"));
        senseOfBeings.add(new SenseOfBeing("work 2", "position 1", "sense 2"));
        senseOfBeings.add(new SenseOfBeing("work 2", "position 1", "sense 3"));
        senseOfBeings.add(new SenseOfBeing("work 4", "position 1", "opposition a"));
        senseOfBeings.add(new SenseOfBeing("work 4", "position 1", "opposition b"));
        senseOfBeings.add(new SenseOfBeing("work 5", "position 1", "opposition c"));
        senseOfBeings.add(new SenseOfBeing("work 5", "position 1", "opposition d"));
        senseOfBeings.add(new SenseOfBeing("work 5", "position 1", "opposition e"));

        System.out.println(System.getProperty("user.dir"));

        Reader reader = new CsvReader();
        reader.read("src/test/resources/test.txt");

        assertEquals(oppositions, reader.getOppositions());
        assertEquals(senseOfBeings, reader.getSensesOfBeing());
    }
}
