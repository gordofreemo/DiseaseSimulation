package DiseaseSim;

/**
 * This interface represents the messages that are passed between the Agents.
 * When an agent receives a message, it passes itself to the doAction method
 * so its state could be modified as need be.
 */



public class MessageAction implements Message {
	Agent agent ;
	
	public MessageAction(Agent agent){
		this.agent=agent;
	}
    public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	public void doAction(Agent agent) {
    	agent.run();
    };
}
