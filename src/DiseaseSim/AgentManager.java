package DiseaseSim;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Class for holding a collection of Agents, initializing them, computing their
 * neighbours, starting them on their own threads, and then probably will
 * communicate with the GUI.
 */

public class AgentManager {

    private ConfigInfo configInfo;
    private ArrayList<Agent> agentList;

    public AgentManager(ConfigInfo configInfo) {
        this.configInfo = configInfo;
        agentList = new ArrayList<>();
    }

    /**
     * Initialize Agents w/ positions specified by board type and give agents some kind of state (sick vs. vulnerable)
     */
    public void initAgents() {
        BoardType board = configInfo.boardType;
        int numSick = configInfo.initSick;
        switch (board) {
            case RANDOM -> initRandom();
        }
        for(Agent agent : agentList) computeNeighbours(agent);

        // Set the randomly sick agents
        Collections.shuffle(agentList); //For randomly assigning the sick
        for(int i = 0; i < numSick; i++) agentList.get(i).setState(AgentState.SICK);
    }

    /**
     * Initialize agents randomly across board
     */
    private void initRandom() {
        int width = configInfo.dimWidth;
        int height = configInfo.dimHeight;
        int numAgents = configInfo.numAgents;

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
        double x_dist = Math.pow((x_1-x_2), 2);
        double y_dist = Math.pow((y_1-y_2), 2);
        return (Math.sqrt(x_dist+y_dist)) <= configInfo.exposureDistance;
    }

    public static void main(String[] args) {
        ConfigInfo info = new ConfigInfo();
        info.exposureDistance = 10;
        info.dimHeight = 50;
        info.dimWidth = 50;
        info.boardType = BoardType.RANDOM;
        info.numAgents = 5;
        AgentManager manager = new AgentManager(info);
        manager.initAgents();
        for(Agent a : manager.agentList) {
            System.out.println(a);
            System.out.print("Neighbours: ");
            System.out.println("");
        }
    }
}
