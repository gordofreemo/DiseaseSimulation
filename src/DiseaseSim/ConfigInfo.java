package DiseaseSim;

/**
 * This class is a container for the various options gathered from the configuration
 * file.
 * Source for having fields just be public variables instead of having getters/
 * setters for all of them:
 * https://www.oracle.com/java/technologies/javase/codeconventions-programmingpractices.html (10.1)
 */


public class ConfigInfo {

    public int dimWidth, dimHeight; //Dimensions of board
    public int exposureDistance;
    public int incubation; //Time it takes until sick
    public int sickness; //Time the agent is sick
    public double recover; //Chance for agent to recover :: (1-recover) = chance to die
    public int initSick; //How many agents are sick from the start?
    public BoardType boardType; //Type of board we have

    // The following variables may be optional depending on the board type, only need to set the ones we need
    public int rows, cols; //How many rows/columns we have if we're on a grid
    public int numAgents; //used in randomgrid and random board type

    // The following is the time in ms for each unit of length
    // For example, if incubation time is 5, then an agent would get sick
    // after 5*unitTime ms. Can be messed around with for better visualization

    public int unitTime;

    @Override
    public String toString() {
        String l1 = "width: " + dimWidth + ", height: " + dimHeight + '\n';
        String l2 = "exposure distance: " + exposureDistance + '\n';
        String l3 = "incubation time: " + incubation + '\n';
        String l4 = "sickness time: " + sickness + '\n';
        String l5 = "recover time: " + recover + '\n';
        String l6 = "initial sick: " + initSick + '\n';
        String l7 = "board type: " + boardType + '\n';
        String l8 = "rows: " + rows + ", columns: " + cols + '\n';
        String l9 = "numAgents: " + numAgents;
        return l1 + l2 + l3 + l4 + l5 + l6 + l7 + l8 + l9;
    }
}
