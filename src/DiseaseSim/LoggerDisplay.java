package DiseaseSim;

import javafx.scene.control.TextArea;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

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

    public void updateScreen() {
        if(!output.isEmpty()) appendText(output.pop() + '\n');
    }

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

    public int getLogSick() {
        return logSick;
    }

    public int getLogImmune() {
        return logImmune;
    }

    public int getLogDead() {
        return logDead;
    }

    public int getNumVulnerable() {
        return numVulnerable;
    }
}
