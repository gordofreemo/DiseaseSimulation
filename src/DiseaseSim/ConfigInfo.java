package DiseaseSim;

/**
 * This class is a container for the various options gathered from the configuration
 * file. Has getter methods for all the options.
 */


public class ConfigInfo {
    /**
     * The type of board that we should have
     */
    public enum BoardType {
        GRID,
        RANDOM,
        RANDOM_GRID
    }

    private int dimWidth, dimHeight; //Dimensions of board
    private int exposureDistance;
    private int incubation; //Time it takes until sick
    private int sickness; //Time the agent is sick
    private double recover; //Chance for agent to recover :: (1-recover) = chance to die
    private int initSick; //How many agents are sick from the start?
    private BoardType boardType; //Type of board we have

    // The following variables may be optional depending on the board type, only need to set the ones we need
    private int rows, cols; //How many rows/columns we have if we're on a grid
    private int numAgents; //used in randomgrid and random board type

    // Could reformat these getting in a cleaner way later on
    public int getDimWidth() {
        return dimWidth;
    }

    public int getDimHeight() {
        return dimHeight;
    }

    public int getExposureDistance() {
        return exposureDistance;
    }

    public int getIncubation() {
        return incubation;
    }

    public int getSickness() {
        return sickness;
    }

    public double getRecover() {
        return recover;
    }

    public int getInitSick() {
        return initSick;
    }

    public BoardType getBoardType() {
        return boardType;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getNumAgents() {
        return numAgents;
    }
}
