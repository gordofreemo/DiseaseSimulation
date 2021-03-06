package DiseaseSim;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

/**
 * Class for constructing the ConfigInfo object given a filename.
 * Constructor throws a FileNotFoundException if the file does not exist
 */

public class FileParser {
    private Scanner sc;
    private ConfigInfo info;

    public FileParser(String fileName) throws FileNotFoundException {
        FileReader file = new FileReader(fileName);
        sc = new Scanner(file);
        info = new ConfigInfo();
    }

    /**
     * Parse the given file and generate the ConfigInfo object
     */
    public void parseFile() {
        while(sc.hasNextLine()) {
            String line = sc.nextLine();
            line = line.toLowerCase();
            if(line == "\n") continue;
            String[] parts = line.split(" ");
            switch (parts[0]) {
                case "dimensions" -> {
                    info.dimWidth  = Integer.parseInt(parts[1]);
                    info.dimHeight = Integer.parseInt(parts[2]);
                }
                case "exposuredistance" -> info.exposureDistance = Integer.parseInt(parts[1]);
                case "incubation" -> info.incubation = Integer.parseInt(parts[1]);
                case "sickness" -> info.sickness = Integer.parseInt(parts[1]);
                case "recover"  -> info.recover = Double.parseDouble(parts[1]);
                case "initialsick" -> info.initSick = Integer.parseInt(parts[1]);
                case "initialimmune" -> info.initImmune = Integer.parseInt(parts[1]);
                case "unittime" -> info.unitTime = Integer.parseInt(parts[1]);
                case "logenabled" -> {
                    if(parts[1].charAt(0) == 't') info.logEnabled = true;
                    else info.logEnabled = false;
                }
                case "grid" -> {
                    info.boardType = BoardType.GRID;
                    info.rows = Integer.parseInt(parts[1]);
                    info.cols = Integer.parseInt(parts[2]);
                    info.numAgents = info.rows * info.cols;
                }
                case "random" -> {
                    info.boardType = BoardType.RANDOM;
                    info.numAgents = Integer.parseInt(parts[1]);
                }
                case "randomgrid" -> {
                    info.boardType = BoardType.RANDOM_GRID;
                    info.rows = Integer.parseInt(parts[1]);
                    info.cols = Integer.parseInt(parts[2]);
                    info.numAgents = Integer.parseInt(parts[3]);
                }
            }
        }
    }

    /**
     * @return - The ConfigInfo object generated from parsing the configuration
     * file
     */
    public ConfigInfo getInfo() {
        return info;
    }
}
