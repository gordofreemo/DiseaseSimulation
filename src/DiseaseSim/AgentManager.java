package DiseaseSim;

import java.util.ArrayList;
import java.util.Collections;

public class AgentManager {

    private ConfigInfo configInfo;
    private ArrayList<Agent> agentList;


    /**
     * Initialize Agents w/ positions specified by board type and give agents some kind of state (sick vs. vulnerable)
     */
    private void initAgents() {
        ConfigInfo.BoardType board = configInfo.getBoardType();
        int numSick = configInfo.getInitSick();
        switch (board) {
            case RANDOM -> initRandom();
        }

        // Set the randomly sick agents - need array of indices to guarantee uniqueness of chosen indices
        ArrayList<Integer> agentIndices = new ArrayList<>();
        for(int i = 0; i < agentList.size(); i++) agentIndices.add(i, i);
        Collections.shuffle(agentIndices);
        for(int i = 0; i < numSick; i++) {
            int index = agentIndices.get(i);
            agentList.get(index).setState(AgentState.SICK);
        }

    }

    /**
     * Initialize agents randomly across board
     */
    private void initRandom() {
        int width = configInfo.getDimWidth();
        int height = configInfo.getDimHeight();
        int numAgents = configInfo.getNumAgents();

        for(int i = 0; i < numAgents; i++) {
            int x = (int)(Math.random() * width);
            int y = (int)(Math.random() * height);
            Agent agent = new Agent(i);
            agent.setPos(x, y);
            agentList.add(i,agent);
        }
    }

}
