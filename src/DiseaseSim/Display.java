package DiseaseSim;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.TimerTask;
import java.util.Timer;

public class Display extends Application {
    private ConfigInfo info;
    private AgentManager manager;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        List<String> args = getParameters().getRaw();
        String filename = "resources/manyAgents.txt";
        if(args.size() != 0) filename = args.get(0);
        info = new ConfigInfo();
        parseInfo(filename);

        manager = new AgentManager(info);
        manager.initAgents();

        VBox box1 = new VBox();
        Canvas canvas = new Canvas();
        Scene scene1 = new Scene(box1);
        canvas.setHeight(info.dimHeight+100);
        canvas.setWidth(info.dimWidth+100);
        box1.getChildren().add(canvas);
        primaryStage.setScene(scene1);

        AgentDrawer drawer = new AgentDrawer(info.dimWidth+100,info.dimHeight+100,10);
        drawer.setOffset(50);

        TimerTask redrawCanvas = new TimerTask() {
            @Override
            public void run() {
                drawer.draw(canvas.getGraphicsContext2D(), manager.getAgents());
            }
        };
        new Timer().scheduleAtFixedRate(redrawCanvas, 0, info.unitTime/2);

        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
            System.exit(0);
        });

        if(info.logEnabled) initLogger();
        manager.startAgents();
    }

    private void initLogger() {
        LoggerDisplay logger = new LoggerDisplay();
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
        logStage.show();
    }

    private void parseInfo(String filename)  {
        try {
            if(filename != "") {
                FileParser parser = new FileParser(filename);
                parser.parseFile();
                info = parser.getInfo();
            }
        } catch (FileNotFoundException e) {
            System.out.println("FILE NOT FOUND / FAILED TO OPEN. EXITING");
            System.exit(1);
        }
    }
}
