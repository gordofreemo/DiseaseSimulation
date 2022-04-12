package DiseaseSim;

import java.util.Collection;
import java.util.concurrent.LinkedBlockingDeque;

public class Agent implements Runnable {
    private MessageBuilder messageBuilder;
    private int xPos, yPos;
    private ConfigInfo configInfo;
    private AgentState state;
    private int id;
    private Collection<Agent> neighbours;
    private LinkedBlockingDeque<Message> messages; //for the messages

    public Agent(int ID, ConfigInfo configInfo) {
        this.id         = ID;
        this.configInfo = configInfo;
        messages        = new LinkedBlockingDeque<>();
        messageBuilder  = new MessageBuilder();
        state           = AgentState.VULNERABLE;
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

    public synchronized Collection<Agent> getNeighbours() {
        return neighbours;
    }

    @Override
    public void run() {
        // If initialized as sick, start the thread to send out "get sick"
        // messages to neighbours, need to make incubation thread
        if(getState() == AgentState.SICK) {
            receiveMessage(messageBuilder.getSick());
        }

        boolean loop = true;
        while(loop) {
            try {
                messages.takeFirst().doAction(this);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            AgentState state = getState();
            loop = state != AgentState.DEAD && state != AgentState.IMMUNE;
        }
    }

    /**
     * Puts a message into the deque of all the neighbours of the current agent
     * @param message - message to be handled by neighbours in the future
     */
    private void sendMessage(Message message) {
        for(Agent neighbour : neighbours) neighbour.receiveMessage(message);
    }


    /**
     * Puts a message into the deque of the current agent
     * @param message - message to be handled by the agent in the future
     */
    private synchronized void receiveMessage(Message message) {
        try {
            messages.putLast(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Agent " + id + " at (" + xPos + "," + yPos + ")";
    }

    /**
     * Class to build the messages that the Agents send between one another.
     * Stateless class (Thread Safe with no Synchronization)
     */
    private class MessageBuilder {
        public Message getSick() {
            return agent -> {
                Runnable event = () -> {
                  agent.setState(AgentState.SICK);
                  System.out.println("Agent " + agent.id + " got sick!");
                  int numLoops = configInfo.sickness;
                  int time     = configInfo.unitTime;
                  for(int i = 0; i < numLoops; i++) {
                      agent.sendMessage(getExposed());
                      try { Thread.sleep(time); }
                      catch (InterruptedException e) { e.printStackTrace(); }
                  }
                  agent.receiveMessage(handleRecovery());
                };
                new Thread(event).start();
            };
        }

        public Message getExposed() {
            return agent -> {
                AgentState state1 = agent.getState();
                if(state1 != AgentState.VULNERABLE) {
                    System.out.println("agent " + agent.id + " got exposed, but is not vulnerable");
                    return;
                }
                agent.setState(AgentState.INCUBATING);
                Runnable event = () -> {
                    System.out.println("agent " + agent.id + " is incubating");
                    int incubation = configInfo.incubation;
                    int time       = configInfo.unitTime;
                    try { Thread.sleep(incubation*time); }
                    catch (InterruptedException e) { e.printStackTrace(); }
                    agent.receiveMessage(getSick());
                };
                new Thread(event).start();
            };
        }

        public Message handleRecovery() {
            return agent -> {
                double roll = Math.random();
                if(roll > configInfo.recover) agent.setState(AgentState.DEAD);
                else agent.setState(AgentState.IMMUNE);
                System.out.println("Agent " + agent.id + " is " + agent.getState());
            };
        }
    }

}
