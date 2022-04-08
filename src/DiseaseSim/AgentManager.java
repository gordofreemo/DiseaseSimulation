package DiseaseSim;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

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

        for(Agent agent : agentList) computeNeighbours(agent);

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

    /**
     * Get and set the neighbours for a particular agent
     * @param a1 - agent to get the neighbours for
     */
    private void computeNeighbours(Agent a1) {
        Collection<Agent> neighbours =
                agentList.stream().filter(a2 ->
                    !(a1.equals(a2)) && exposedClose(a1, a2)
                ).collect(Collectors.toUnmodifiableList());
        a1.setNeighbours(neighbours);
    }

    /**
     * Computes whether two agents are within exposure distance of one another
     * @param a1 - one of the agents used in the check
     * @param a2 - other agent used in the check
     * @return - return true if within exposure distance, false otherwise
     */
    private boolean exposedClose(Agent a1, Agent a2) {
        int x_1 = a1.getXPos();
        int x_2 = a2.getXPos();
        int y_1 = a1.getYPos();
        int y_2 = a2.getYPos();
        double x_dist = (x_1-x_2)^2;
        double y_dist = (y_1-y_2)^2;
        return (Math.sqrt(x_dist+y_dist)) <= configInfo.getExposureDistance();
    }
}
