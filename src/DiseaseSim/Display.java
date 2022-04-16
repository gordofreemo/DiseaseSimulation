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

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        List<String> args = getParameters().getRaw();
        String filename = "resources/randomGridTest.txt";
        if(args.size() != 0) filename = args.get(0);
        FileParser parser;
        ConfigInfo info = new ConfigInfo();
        LoggerDisplay logger = new LoggerDisplay();
        try {
            if(filename != "") {
                parser = new FileParser(filename);
                parser.parseFile();
                info = parser.getInfo();
            }
        } catch (FileNotFoundException e) {
            System.out.println("FILE NOT FOUND / FAILED TO OPEN. EXITING");
            System.exit(1);
        }
        AgentManager manager = new AgentManager(info);
        manager.initAgents();
        manager.addLogger(logger);

        Stage newStage = new Stage();
        VBox box1 = new VBox();
        Scene scene1 = new Scene(box1);
        Scene scene2 = new Scene(logger);
        Canvas canvas = new Canvas();
        canvas.setHeight(info.dimHeight+100);
        canvas.setWidth(info.dimWidth+100);
        box1.getChildren().add(canvas);

        AgentDrawer drawer = new AgentDrawer(info.dimWidth+100,info.dimHeight+100,10);
        drawer.setOffset(50);
        primaryStage.setScene(scene1);
        newStage.setScene(scene2);

        TimerTask redrawCanvas = new TimerTask() {
            @Override
            public void run() {
                drawer.draw(canvas.getGraphicsContext2D(), manager.getAgents());
            }
        };

        TimerTask redrawLog = new TimerTask() {
            @Override
            public void run() {
                 Platform.runLater(() -> logger.updateScreen());
            }
        };

        new Timer().scheduleAtFixedRate(redrawCanvas, 0, info.unitTime/2);
        new Timer().scheduleAtFixedRate(redrawLog, 0, 10);
        primaryStage.show();
        newStage.show();
        primaryStage.setOnCloseRequest(event -> {
            System.exit(0);
        });
        manager.startAgents();
    }
}
