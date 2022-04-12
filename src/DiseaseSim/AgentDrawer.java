package DiseaseSim;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.Collection;

/**
 * Class for drawing a collection of agents onto some GraphicsContext
 * (probably a canvas). Initialize the constructor with a size - radius of
 * circles being drawn, and then call draw with a graphics context and collection
 * of agents, and it will draw the agents on the graphics context.
 */

public class AgentDrawer {
    private Collection<AgentToGUI> agentsGUI;
    private double size; //size of radius of circle representing agent (in px)

    AgentDrawer(double size) {
        this.size = size;
    }

    public void draw(GraphicsContext g, Collection<Agent> agents) {
        mapAgents(agents);
        for(AgentToGUI agent : agentsGUI) {
            g.setFill(agent.color);
            g.fillOval(agent.xPos-size,agent.yPos-size,size,size);
        }
    }

    private void mapAgents(Collection<Agent> agents) {
        agentsGUI = agents.stream().map(a -> mapAgent(a)).toList();
    }

    private AgentToGUI mapAgent(Agent agent) {
        Color color;
        switch(agent.getState()) {
            case IMMUNE -> color = Color.LIGHTBLUE;
            case DEAD   -> color = Color.BLACK;
            case SICK   -> color = Color.GREEN;
            default     -> color = Color.DARKBLUE;
        }

        return new AgentToGUI(color, agent.getXPos(), agent.getYPos());
    }

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
