import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

class Main {
    private static String fileName = "data.txt";

    public static void main(String[] args) {
        loadFile();
    }

    private static void loadFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line = reader.readLine();
            Long[] currSeedMap = Arrays.stream(line.split(": ")[1].split(" ")).map(Long::parseLong).toArray(Long[]::new);
            List<Range> seedRanges = new ArrayList<>();
            for (int i = 0; i < currSeedMap.length; i += 2) {
                seedRanges.add(new Range(currSeedMap[i], currSeedMap[i] + currSeedMap[i + 1]));
            }
            System.out.println(seedRanges);

            // Skip two lines
            reader.readLine();
            reader.readLine();

            List<Range> sourceRanges = new ArrayList<>();
            List<Range> destinationRanges = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) {
                    continue;
                } else if (line.contains("map:")) {
                    if (!sourceRanges.isEmpty()) {
                        List<Range> newSeedRanges = new ArrayList<>();
                        List<Range> updatedRanges = Range.mapRanges(seedRanges, sourceRanges, destinationRanges);
                        newSeedRanges.addAll(updatedRanges);

                        seedRanges = newSeedRanges;
                        System.out.println(seedRanges);
                    }
                    // Reset for the next set of mappings
                    sourceRanges.clear();
                    destinationRanges.clear();
                } else {
                    Long[] mapLongs = Arrays.stream(line.split(" ")).map(Long::parseLong).toArray(Long[]::new);
                    sourceRanges.add(new Range(mapLongs[1], mapLongs[1] + mapLongs[2]));
                    destinationRanges.add(new Range(mapLongs[0], mapLongs[0] + mapLongs[2]));
                }
            }
            Optional<Range> minRange = seedRanges.stream()
                    .min(Comparator.comparingLong(r -> r.start));

            System.out.println(minRange.get());

        } catch (IOException e) {
            System.out.println("Error loading data from file: " + fileName + "\n" + e.getMessage());
        }
    }



}
