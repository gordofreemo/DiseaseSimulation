package DiseaseSim;

import java.util.*;

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
        agentList       = new ArrayList<>();
    }

    /**
     * Initialize Agents w/ positions specified by board type and give agents some kind of state (sick vs. vulnerable)
     */
    public void initAgents() {
        BoardType board = configInfo.boardType;
        int numSick     = configInfo.initSick;
        switch (board) {
            case RANDOM -> initRandom();
            case GRID   -> initGrid();
            case RANDOM_GRID -> initRandomGrid();
        }
        for(Agent agent : agentList) computeNeighbours(agent);

        // Set the randomly sick agents
        Collections.shuffle(agentList); //For randomly assigning the sick
        for(int i = 0; i < numSick; i++) agentList.get(i).setState(AgentState.SICK);
    }

    public void startAgents() {
        for(Agent agent : agentList) new Thread(agent).start();
    }

    // Doesn't work currently
    public void stopAgents() {
        for(Agent agent : agentList) agent.softCloseThread();
    }

    /**
     * Initialize agents randomly across board
     */
    private void initRandom() {
        int width     = configInfo.dimWidth;
        int height    = configInfo.dimHeight;
        int numAgents = configInfo.numAgents;

        for(int i = 0; i < numAgents; i++) {
            int x = (int)(Math.random() * width);
            int y = (int)(Math.random() * height);
            Agent agent = new Agent(i, configInfo);
            agent.setPos(x, y);
            agentList.add(i,agent);
        }
    }

    /**
     * Initialize agents on a grid
     */
    private void initGrid() {
        int r = configInfo.rows;
        int c = configInfo.cols;
        int dis = configInfo.exposureDistance;
        int id = 0;
        for(int i = 0; i < r; i++) {
            for(int j = 0; j < c; j++) {
                Agent agent = new Agent(id, configInfo);
                agent.setPos(j*dis, i*dis);
                agentList.add(i,agent);
                id++;
            }
        }
    }

    private void initRandomGrid() {
        int r = configInfo.rows;
        int c = configInfo.cols;
        int dist = configInfo.exposureDistance;
        ArrayList<Tuple<Integer, Integer>> coordinates = new ArrayList<>();

        for(int i = 0; i < r; i++) {
            for(int j = 0; j < c; j++) {
                coordinates.add(new Tuple<>(i*dist,j*dist));
            }
        }
        Collections.shuffle(coordinates);
        int id = 0;
        for(int i = 0; i < configInfo.numAgents; i++) {
            Tuple<Integer, Integer> cord = coordinates.remove(0);
            Agent agent = new Agent(id, configInfo);
            agent.setPos(cord.x, cord.y);
            agentList.add(i,agent);
            id++;
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
                ).toList();
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

    public Collection<Agent> getAgents() {
        return agentList;
    }


    public static void main(String[] args) {
        ConfigInfo info       = new ConfigInfo();
        info.recover          = 0.95;
        info.exposureDistance = 20;
        info.dimHeight        = 5;
        info.dimWidth         = 5;
        info.boardType        = BoardType.RANDOM;
        info.numAgents        = 6;
        info.initSick         = 1;
        info.unitTime         = 1000;
        info.incubation       = 5;
        info.sickness         = 5;

        AgentManager manager  = new AgentManager(info);
        manager.initAgents();
        for(Agent a : manager.agentList) {
            System.out.println(a + " " + a.getState());
            System.out.print("Neighbours: ");
            for(Agent b : a.getNeighbours()) System.out.print(" " + b + " ");
            System.out.println();
        }
        manager.startAgents();
    }
}
