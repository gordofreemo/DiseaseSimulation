package DiseaseSim;

/**
 * Represents the various states of the agent.
 * Used by various classes for standardized method of communication.
 * The one exception to having the states being represented exactly is
 * the Agent class will send that they are "incubating" to the logger
 * object if they started out immune. This fixed a very big bug.
 */

public enum AgentState {
    VULNERABLE,
    INCUBATING,
    SICK,
    IMMUNE,
    DEAD
}
