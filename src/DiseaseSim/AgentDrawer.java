package DiseaseSim;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.Collection;

/**
 * Class for drawing a collection of agents onto some GraphicsContext
 * (probably a canvas). Initialize the constructor with a size - radius of
 * circles being drawn, and then call draw with a graphics context and collection
 * of agents, and it will draw the agents on the graphics context.
 */

public class AgentDrawer extends Canvas {
    private Collection<AgentToGUI> agentsGUI;
    private double size; //size of radius of circle representing agent (in px)
    private int width;
    private int height;
    private int offset;

    AgentDrawer(int width, int height, double size) {
        super(width,height);
        this.width = width;
        this.height = height;
        this.size = size;
        offset = 0;
    }

    /**
     * @param offset - sets how far from the top left corner of the canvas
     *               this class should draw Agents
     */
    public void setOffset(int offset) {
        this.offset = offset;
    }

    /**
     * Repaint the given collection of agents onto the screen
     * @param agents - agents to draw onto the screen
     */
    public void draw(Collection<Agent> agents) {
        GraphicsContext g = this.getGraphicsContext2D();
        mapAgents(agents);
        g.clearRect(0,0,width,height);
        for(AgentToGUI agent : agentsGUI) {
            g.setFill(agent.color);
            g.fillOval(agent.xPos+offset/2,agent.yPos+offset/2,size,size);
        }
    }

    /**
     * Given a collection of agents, transform the agents into a format that
     * makes them easy to paint on the screen
     * @param agents - collection of Agents to draw
     */
    private void mapAgents(Collection<Agent> agents) {
        agentsGUI = agents.stream().map(a -> mapAgent(a)).toList();
    }

    /**
     * Convert an Agent to a format that makes them easier to paint on the
     * screen
     * @param agent - agent to convert to drawing format
     * @return - returns an AgentToGUI object representing the agent in
     * a way that makes it easier to draw on the screen.
     */
    private AgentToGUI mapAgent(Agent agent) {
        Color color;
        switch(agent.getState()) {
            case IMMUNE -> color = Color.LIGHTBLUE;
            case DEAD   -> color = Color.BLACK;
            case SICK   -> color = Color.RED;
            default     -> color = Color.DARKBLUE;
        }
        return new AgentToGUI(color, agent.getXPos(), agent.getYPos());
    }

    /**
     * Class represent a "graphical" representation of an agent.
     * Is essentially just a container for an (x,y) position and a color.
     */
    private class AgentToGUI {
        public Color color;
        public int xPos;
        public int yPos;
        public AgentToGUI(Color color, int xPos, int yPos) {
            this.color = color;
            this.xPos  = xPos;
            this.yPos  = yPos;
        }
    }
}
