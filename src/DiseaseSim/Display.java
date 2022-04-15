package DiseaseSim;

import javafx.application.Application;
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
        String filename = "resources/grid.txt";
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

        VBox box = new VBox();
        Scene scene = new Scene(box);
        Canvas canvas = new Canvas();
        canvas.setHeight(info.dimHeight);
        canvas.setWidth(info.dimWidth);
        box.getChildren().add(canvas);

        AgentManager manager = new AgentManager(info);
        manager.addLogger(logger);
        AgentDrawer drawer = new AgentDrawer(info.dimWidth,info.dimHeight,10);
        manager.initAgents();
        primaryStage.setScene(scene);

        TimerTask redraw = new TimerTask() {
            @Override
            public void run() {
                drawer.draw(canvas.getGraphicsContext2D(), manager.getAgents());
            }
        };

        new Timer().scheduleAtFixedRate(redraw, 0, info.unitTime/3);
        manager.startAgents();
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
            System.exit(0);
        });
    }
}
