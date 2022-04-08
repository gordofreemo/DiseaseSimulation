package DiseaseSim;

import java.util.Collection;

public class Agent {
    private int xPos, yPos;
    private AgentState state;
    private int id;
    private Collection<Agent> neighbours;

    public Agent(int ID) {
        this.id = id;
    }

    public synchronized void setPos(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public synchronized int getXPos() {
        return xPos;
    }

    public synchronized int getYPos() {
        return yPos;
    }

    public int getID() {
        return id;
    }

    public synchronized void setState(AgentState state) {
        this.state = state;
    }

    public synchronized AgentState getState() {
        return state;
    }

    public synchronized void setNeighbours(Collection<Agent> neighbours) {
        this.neighbours = neighbours;
    }
}
