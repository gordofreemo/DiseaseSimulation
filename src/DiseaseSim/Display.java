package DiseaseSim;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.TimerTask;
import java.util.Timer;

public class Display extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        VBox box = new VBox();
        Scene scene = new Scene(box);
        Canvas canvas = new Canvas();
        canvas.setHeight(500);
        canvas.setWidth(500);
        box.getChildren().add(canvas);

        ConfigInfo info = new ConfigInfo();
        info.recover = 0.95;
        info.exposureDistance = 30;
        info.dimHeight = 300;
        info.dimWidth = 300;
        info.boardType = BoardType.RANDOM_GRID;
        info.numAgents = 50;
        info.rows = 10;
        info.cols = 10;
        info.initSick = 5;
        info.unitTime = 200;
        info.incubation = 5;
        info.sickness = 5;

        AgentManager manager = new AgentManager(info);
        AgentDrawer drawer = new AgentDrawer(500,500,15);
        manager.initAgents();
        primaryStage.setScene(scene);

        TimerTask redraw = new TimerTask() {
            @Override
            public void run() {
                drawer.draw(canvas.getGraphicsContext2D(), manager.getAgents());
            }
        };

        new Timer().scheduleAtFixedRate(redraw, 0, info.unitTime/2);
        manager.startAgents();
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
            manager.stopAgents();
        });
    }
}