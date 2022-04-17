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
        logSick = info.initSick;
        logImmune = info.initImmune;
        logDead = 0;
        numVulnerable = info.numAgents - logImmune - logSick;
    }

    public void updateScreen() {
        if(!output.isEmpty()) appendText(output.pop() + '\n');
    }

    public void receiveUpdate(String update, AgentState state) {
        try {
            output.putFirst(update);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (this) {
            switch(state) {
                case IMMUNE -> logImmune++;
                case DEAD   -> logDead++;
                case SICK   -> {logSick++; numVulnerable--;}
            }
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
