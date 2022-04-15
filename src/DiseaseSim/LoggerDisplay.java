package DiseaseSim;

import javafx.scene.control.TextArea;

public class LoggerDisplay extends TextArea {
    private boolean state; // If true, actually appends to display

    public LoggerDisplay() {
        super();
        this.setEditable(false);
        state = false;
    }

    public void holdState(boolean state) {
        this.state = state;
    }
    public void receiveUpdate(String update) {
        System.out.println(update);
        if(state) this.appendText(update);
    }

}
