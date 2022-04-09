package DiseaseSim;

import java.util.Collection;
import java.util.concurrent.LinkedBlockingDeque;

public class Agent implements Runnable {
    private int xPos, yPos;
    private AgentState state;
    private int id;
    private Collection<Agent> neighbours;
    private LinkedBlockingDeque<Message> messages; //for the messages

    public Agent(int ID) {
        this.id = ID;
        messages = new LinkedBlockingDeque<>();
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


    public synchronized void setState(AgentState state) {
        this.state = state;
    }

    public synchronized AgentState getState() {
        return state;
    }

    public synchronized void setNeighbours(Collection<Agent> neighbours) {
        this.neighbours = neighbours;
    }

    @Override
    public void run() {
        while(getState() != AgentState.DEAD) {

        }
    }

    @Override
    public String toString() {
        return "Agent " + id + " at (" + xPos + "," + yPos + ")";
    }

}
