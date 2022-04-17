package DiseaseSim;

import javafx.scene.control.TextArea;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Class for making the pane with log information about the various
 * state changes for the agents.
 */

public class LoggerDisplay extends TextArea {
    private BlockingDeque<String> output;
    private int logSick;
    private int logImmune;
    private int logDead;
    private int numVulnerable;


    public LoggerDisplay(ConfigInfo info) {
        super();
        output = new LinkedBlockingDeque<>();
        setPrefSize(500,500);
        logSick = 0;
        logImmune = 0;
        logDead = 0;
        numVulnerable = info.numAgents-info.initImmune;
    }

    /**
     * Adds a new log to the screen if there is a backlog of logs to add
     */
    public void updateScreen() {
        if(!output.isEmpty()) appendText(output.pop() + '\n');
    }

    /**
     * Method for Agents to add a log to the BlockingQueue
     * @param update - String of log to add
     * @param state - State the agent changed to, used to update counts
     */
    public synchronized void receiveUpdate(String update, AgentState state) {
        try {
            output.putFirst(update);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // I have used the incubating state to denote "initially immune"
        switch(state) {
            case IMMUNE -> {logImmune++; logSick--;}
            case DEAD   -> {logDead++; logSick--;}
            case SICK   -> {logSick++; numVulnerable--;}
            case INCUBATING -> logImmune++;
        }
    }

    /**
     * @return - number of currently sick agents
     */
    public int getLogSick() {
        return logSick;
    }

    /**
     * @return - number of currently immune agents
     */
    public int getLogImmune() {
        return logImmune;
    }

    /**
     * @return - number of currently dead agents
     */
    public int getLogDead() {
        return logDead;
    }

    /**
     * @return - number of currently vulnerable agents
     */
    public int getNumVulnerable() {
        return numVulnerable;
    }
}
