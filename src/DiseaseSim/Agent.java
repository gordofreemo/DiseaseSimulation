package DiseaseSim;

import java.util.Collection;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Represents an Agent running on its own thread. Communicates with its neighbours
 * via a blocking queue and who's sole duty is to send and read message,
 * and allow other classes to access its fields in a thread-safe manner.
 */

public class Agent implements Runnable {
    private MessageBuilder messageBuilder;
    private int xPos, yPos;
    private ConfigInfo configInfo;
    private AgentState state;
    private int id;
    private Collection<Agent> neighbours;
    private LinkedBlockingDeque<Message> messages; //for the messages
    private LoggerDisplay logger;

    public Agent(int ID, ConfigInfo configInfo) {
        this.id         = ID;
        this.configInfo = configInfo;
        messages        = new LinkedBlockingDeque<>();
        messageBuilder  = new MessageBuilder();
        state           = AgentState.VULNERABLE;
    }

    /**
     * Sets the coordinates of the agent on the screen
     * @param xPos - x-coordinate
     * @param yPos - y-coordinate
     */
    public synchronized void setPos(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }

    /**
     * @return - x-coordinate of the agent
     */
    public synchronized int getXPos() {
        return xPos;
    }

    /**
     * @return - y-coordinate of the agent
     */
    public synchronized int getYPos() {
        return yPos;
    }


    /**
     * @param state - the state of the agent, whether immune, sick, etc.
     */
    public synchronized void setState(AgentState state) {
        this.state = state;
    }

    /**
     * @return - the state of the agent, whether immune, sick, etc.
     */
    public synchronized AgentState getState() {
        return state;
    }

    /**
     * @param neighbours - Collection of other agents with which this agent is
     *                   "exposure distance" close to.
     */
    public synchronized void setNeighbours(Collection<Agent> neighbours) {
        this.neighbours = neighbours;
    }

    /**
     * @return - Collection of "exposure close" agents
     */
    public synchronized Collection<Agent> getNeighbours() {
        return neighbours;
    }

    /**
     * @param logger - the logging object which an agent alerts whenever
     *               its state is changed
     */
    public void setLogger(LoggerDisplay logger) {
        this.logger = logger;
    }

    @Override
    public void run() {
        // If initialized as sick, start the thread to send out "get sick"
        // messages to neighbours, need to make incubation thread
        if(getState() == AgentState.SICK) {
            receiveMessage(messageBuilder.getSick(this.id));
        }
        if(getState() == AgentState.IMMUNE) {
            String message = "Agent " + id + " is IMMUNE";
            logMessage(message, AgentState.INCUBATING);
        }

        boolean loop = true;
        //While agent is not dead, read messages from the queue
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
    private void receiveMessage(Message message) {
        try {
            messages.putLast(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Called to update the logging object when the state of the agent changes.
     * @param message - String representing the logging message to send
     * @param state - The state that the agent just transformed to
     */
    private void logMessage(String message, AgentState state) {
        if(logger != null) logger.receiveUpdate(message, state);
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
        /**
         * Makes a message that forces the current agent to get sick
         * @param callerID - agent that sent this message
         * @return - returns a message that when run, starts a new thread which
         * after a certain amount of time changes the state of the agent to sick.
         */
        public Message getSick(int callerID) {
            return agent -> {
                Runnable event = () -> {
                  agent.setState(AgentState.SICK);
                  logMessage("Agent " + agent.id + " got sick!", AgentState.SICK);
                  int numLoops = configInfo.sickness;
                  int time     = configInfo.unitTime;
                  for(int i = 0; i < numLoops; i++) {
                      agent.sendMessage(getExposed(callerID));
                      try { Thread.sleep(time); }
                      catch (InterruptedException e) { e.printStackTrace(); }
                  }
                  agent.receiveMessage(handleRecovery());
                };
                new Thread(event).start();
            };
        }

        /**
         * Makes a message that exposes all the neighbours to sickness
         * @param callerID - ID of agent which sent this message
         * @return - returns a message that when run, generates a new thread
         * to alert all the neighbours of the agent to get sick
         */
        public Message getExposed(int callerID) {
            return agent -> {
                AgentState state1 = agent.getState();
                if(state1 != AgentState.VULNERABLE) return;

                agent.setState(AgentState.INCUBATING);
                Runnable event = () -> {
                    int incubation = configInfo.incubation;
                    int time       = configInfo.unitTime;
                    try { Thread.sleep(incubation*time); }
                    catch (InterruptedException e) { e.printStackTrace(); }
                    agent.receiveMessage(getSick(callerID));
                };
                new Thread(event).start();
            };
        }

        /**
         * Makes a message that when run, decides whether the agent becomes
         * IMMUNE or does DEAD and changes state accordingly
         * @return - message that changes post-sickness state of agent
         */
        public Message handleRecovery() {
            return agent -> {
                double roll = Math.random();
                AgentState state = AgentState.IMMUNE;
                if(roll > configInfo.recover) {
                    agent.setState(AgentState.DEAD);
                    state = AgentState.DEAD;
                }
                else agent.setState(AgentState.IMMUNE);
                String str = "Agent " + agent.id + " is " + agent.getState();
                logMessage(str,state);
            };
        }
    }

}
