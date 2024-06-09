# it-analyzer
Reads a tab-separated CSV file or a JSON file with the columns:

- work
- position
- text
- relevant
- opposition
- senses

it-analyzer analyzes the file and saves its results in the following files:
- senses.txt, containing the number of times a sense of being appears in Aquinas's corpus
- oppositions.txt, containing the number of times an opposition appears in Aquinas's corpus
- senses-per-work.txt, containing the number of senses of being per work
- oppositions-per-work.txt, containing the number of oppositions per work
- positions-per-sense.txt, containing the positions where a sense of being appears in Aquinas's corpus
- positions-per-opposition.txt, containing the positions where an opposition appears in Aquinas's corpus

## How to Build and Run it-analyzer

```
mvn clean install

java -jar target/it-analyzer-1.0.0.jar input_file
```

where `input_file` is the file to be read. 

For example:

```
java -jar target/it-analyzer-1.0.0.jar entries.csv
```
