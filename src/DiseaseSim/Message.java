package DiseaseSim;

/**
 * This interface represents the messages that are passed between the Agents.
 * When an agent receives a message, it passes itself to the doAction method
 * so its state could be modified as need be.
 */

@FunctionalInterface

public interface Message {
    void doAction(Agent agent);
}
