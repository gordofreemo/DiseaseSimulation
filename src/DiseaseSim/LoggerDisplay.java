package DiseaseSim;

import javafx.scene.control.TextArea;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class LoggerDisplay extends TextArea {
    private BlockingDeque<String> output;

    public LoggerDisplay() {
        super();
        output = new LinkedBlockingDeque<>();
        setPrefSize(500,500);
    }

    public void updateScreen() {
        if(!output.isEmpty()) appendText(output.pop() + '\n');
    }

    public void receiveUpdate(String update) {
        try {
            output.putFirst(update);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
