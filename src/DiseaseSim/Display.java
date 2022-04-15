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
        canvas.setHeight(900);
        canvas.setWidth(900);
        box.getChildren().add(canvas);

        ConfigInfo info = new ConfigInfo();
        info.recover = 0.95;
        info.exposureDistance = 20;
        info.dimHeight = 900;
        info.dimWidth = 900;
        info.boardType = BoardType.RANDOM;
        info.numAgents = 1000;
        info.rows = 10;
        info.cols = 10;
        info.initSick = 100;
        info.initImmune = 100;
        info.unitTime = 200;
        info.incubation = 5;
        info.sickness = 5;

        AgentManager manager = new AgentManager(info);
        AgentDrawer drawer = new AgentDrawer(900,900,10);
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
            System.exit(0);
        });
    }
}
