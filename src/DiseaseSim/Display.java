package DiseaseSim;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.TimerTask;
import java.util.Timer;

/**
 * Main driver of program, starts up the graphical interface
 * and all the other components.
 */

public class Display extends Application {
    private ConfigInfo info;
    private AgentManager manager;
    private GraphDisplay graph;
    private LoggerDisplay logger;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        List<String> args = getParameters().getRaw();
        String filename = "";
        if(args.size() != 0) filename = args.get(0);
        info = new ConfigInfo();
        parseInfo(filename);

        manager = new AgentManager(info);
        manager.initAgents();
        AgentDrawer drawer = new AgentDrawer(info.dimWidth+100,info.dimHeight+100,10);
        drawer.setOffset(50);

        VBox box1 = new VBox();
        Scene scene1 = new Scene(box1);
        box1.getChildren().add(drawer);
        primaryStage.setScene(scene1);


        TimerTask redrawCanvas = new TimerTask() {
            @Override
            public void run() {
                drawer.draw(manager.getAgents());
            }
        };
        new Timer().scheduleAtFixedRate(redrawCanvas, 0, info.unitTime/2);

        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
            System.exit(0);
        });

        //Start up the graph/logger only if logging is enabled
        if(info.logEnabled) { initLogger(); initGraph(); }
        manager.startAgents();
    }

    /**
     * Initializes the graph showing how many agents have what kind of
     * state at what point in time.
     */
    private void initGraph() {
        graph = new GraphDisplay(300, 300, logger);
        VBox graphHolder = new VBox(graph);
        graphHolder.setPrefSize(300,300);
        Stage graphStage = new Stage();
        Scene graphScene = new Scene(graphHolder);
        graph.repaint();
        graphStage.setScene(graphScene);
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        graphStage.setX((bounds.getWidth()) / 8);
        TimerTask redrawGraph = new TimerTask() {
            @Override
            public void run() {
                graph.repaint();
            }
        };
        new Timer().scheduleAtFixedRate(redrawGraph, 0, info.unitTime);
        graphStage.show();
    }

    /**
     * Start up the logging object to display text alerts of when the
     * states of certain agents change.
     */
    private void initLogger() {
        logger = new LoggerDisplay(info);
        manager.addLogger(logger);
        Stage logStage = new Stage();
        Scene logScene = new Scene(logger);
        logStage.setScene(logScene);
        TimerTask redrawCanvas = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> logger.updateScreen());
            }
        };
        new Timer().scheduleAtFixedRate(redrawCanvas, 0, 10);
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        logStage.setX(bounds.getWidth() - (bounds.getWidth()/3));
        logStage.show();
    }

    /**
     * Parse a file and make a configuration info objection from it
     * @param filename - file to open for parsing
     */
    private void parseInfo(String filename)  {
        try {
            if(filename != "") {
                FileParser parser = new FileParser(filename);
                parser.parseFile();
                info = parser.getInfo();
            }
        } catch (FileNotFoundException e) {
            System.out.println("FILE NOT FOUND / FAILED TO OPEN. DEFAULT SETTINGS");
            info = new ConfigInfo();
        }
    }
}
