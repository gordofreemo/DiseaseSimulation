package DiseaseSim;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.LinkedList;

public class GraphDisplay extends Canvas {
    private LinkedList<Integer> sickPoints;
    private LinkedList<Integer> vulnerablePoints;
    private LinkedList<Integer> immunePoints;
    private LinkedList<Integer> deadPoints;

    private LoggerDisplay logger;
    private int unitsElapsed;
    private int width;
    private int height;
    private double vOffset;
    private double hOffset;
    private double vSpacing;
    private double hSpacing;
    private int numSick;
    private int numVulnerable;
    private int numImmune;
    private int numDead;
    private int totalAgents;

    public GraphDisplay(int width, int height, LoggerDisplay logger) {
        super(width, height);
        this.width = width;
        this.height = height;
        this.logger = logger;
        sickPoints = new LinkedList<>();
        vulnerablePoints = new LinkedList<>();
        immunePoints = new LinkedList<>();
        deadPoints = new LinkedList<>();
        unitsElapsed = 1;
        vOffset = height/10;
        hOffset = width/10;
        vSpacing = (height-vOffset)/10;
        hSpacing = (width-hOffset)/10;
        numSick = logger.getLogSick();
        numVulnerable = logger.getNumVulnerable();
        numDead = logger.getLogDead();
        numImmune = logger.getLogImmune();
        totalAgents = numSick + numVulnerable + numDead + numImmune;
    }

    public void repaint() {
        unitsElapsed++;
        getGraphicsContext2D().clearRect(0,0,width,height);
        updateDead();
        updateImmune();
        updateSick();
        updateVulnerable();
        drawOuterPart();
        drawLine(deadPoints, AgentState.DEAD);
        drawLine(sickPoints, AgentState.SICK);
        drawLine(vulnerablePoints, AgentState.VULNERABLE);
        drawLine(immunePoints, AgentState.IMMUNE);
    }

    private void drawLine(LinkedList<Integer> points, AgentState state) {
        GraphicsContext g = this.getGraphicsContext2D();
        switch(state) {
            case SICK -> {g.setStroke(Color.RED); g.setFill(Color.RED);}
            case IMMUNE -> {g.setStroke(Color.LIGHTBLUE); g.setFill(Color.LIGHTBLUE);}
            case DEAD -> {g.setStroke(Color.BLACK); g.setFill(Color.BLACK);}
            default -> {g.setStroke(Color.DARKBLUE); g.setFill(Color.DARKBLUE);}
        }

        double previousX = 0;
        double previousY = 0;
        for(int i = 0; i < points.size(); i++) {
            int y = points.get(i);
            double yCord = (((double)y)/totalAgents) * height;
            yCord = height - (yCord+vOffset+2.5);
            double xCord  = hSpacing*i+hOffset-2.5;
            g.fillOval(xCord, yCord, 5, 5);
            if(i != 0) {
                g.strokeLine(previousX+2.5, previousY+2.5, xCord+2.5, yCord+2.5);
            }
            previousX = xCord;
            previousY = yCord;
        }
    }

    private void drawOuterPart() {
        GraphicsContext g = this.getGraphicsContext2D();
        g.setStroke(Color.BLACK);
        g.setFill(Color.BLACK);
        g.strokeLine(hOffset, 0, hOffset, height-vOffset);
        g.strokeLine(hOffset, height-vOffset, width, height-vOffset);
        for(int i = 0; i < 10; i++) {
            double hNudge = width/50;
            double vNudge = height/50;
            g.strokeLine(hOffset-hNudge, height - (vSpacing*i + vOffset),
                         hOffset+hNudge, height - (vSpacing*i + vOffset));
            g.strokeLine(hSpacing*i + hOffset, height - (vOffset - vNudge),
                         hSpacing*i + hOffset, height - (vOffset + vNudge));
            g.fillText("" + (i+unitsElapsed), hSpacing*i + hOffset, height - (hNudge));
            g.fillText("" + i*(totalAgents/10), 0, height - (vSpacing * i + vOffset));
        }
    }

    private void updateSick() {
        if(sickPoints.size() == 10) sickPoints.pop();
        sickPoints.addLast(logger.getLogSick());
    }

    private void updateVulnerable() {
        if(vulnerablePoints.size() == 10) vulnerablePoints.pop();
        vulnerablePoints.addLast(logger.getNumVulnerable());
    }

    private void updateImmune() {
        if(immunePoints.size() == 10) immunePoints.pop();
        immunePoints.addLast(logger.getLogImmune());
    }

    private void updateDead() {
        if(deadPoints.size() == 10) deadPoints.pop();
        deadPoints.addLast(logger.getLogDead());
    }
}
