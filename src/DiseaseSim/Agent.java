package DiseaseSim;

public class Agent {
    private int xPos, yPos;
    private AgentState state;
    private int id;

    public Agent(int ID) {
        this.id = id;
    }

    public synchronized void setPos(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
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
}
