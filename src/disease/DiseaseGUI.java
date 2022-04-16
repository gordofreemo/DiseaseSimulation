package disease;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingDeque;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;

import DiseaseSim.Agent;
import DiseaseSim.Message;
import DiseaseSim.MessageAction;

/**
 * class DiseaseGUI
 *
 * main window for ant simulation contains: 1. a control panel for setting up
 * and running the simulation 2. a graphical view of the ant colony
 */
public class DiseaseGUI extends JFrame {

	/*************
	 * attributes
	 ************/

	// view for colony
	private AgentView colonyView;

	// scroll pane for colonyView
	private JScrollPane colonyPane;

	// panel containing buttons for controlling simulation
	private ControlPanel controlPanel;

	// layout for positioning child components
	private SpringLayout layout;

	// user's screen width
	private int screenWidth;

	// user's screen height
	private int screenHeight;

	// list of event listeners for this view
	private LinkedList simulationEventListenerList;

	static int step = 1;

	public static LinkedBlockingDeque<Message> msg_que = new LinkedBlockingDeque<Message>();
	public AgentDisplay agentdisplay = new AgentDisplay(msg_que);

	/**
	 * create a new AntSimGUI
	 */
	public DiseaseGUI() {
		// call superclass constructor and set title of window
		super("disease Simulation GUI");

		// create anonymous listener to allow user to close window and end sim
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				System.exit(0);
			}
		});

		// get user's screen width and height
		screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		screenHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();

		// set layout
		getContentPane().setLayout(new BorderLayout());

		// set the size of the window
		resizeGUI();

		// create event listener list
		simulationEventListenerList = new LinkedList();
		// simulationEventListenerList.add(new InitListener());

		simulationEventListenerList.add(new RunListener());

		// show window
		setVisible(true);

		// validate all components
		validate();
	}

	/**********
	 * methods
	 *********/

	/**
	 * initialize this GUI
	 *
	 * a control panel and scrollable pane for displaying the specified ColonyView
	 * will be created and added to this GUI
	 *
	 * @param colonyView the ColonyView to be displayed
	 */
	public void initGUI(AgentView colonyView) {
		// create button control panel
		controlPanel = new ControlPanel();

		// set up colony view with default dimensions
		colonyPane = new JScrollPane(colonyView);
		colonyPane.setPreferredSize(new Dimension(200, 200));

		// add control panel and colony view
		getContentPane().add(controlPanel, BorderLayout.NORTH);
		getContentPane().add(colonyPane, BorderLayout.CENTER);

		// validate all components
		validate();
	}

	/**
	 * set window size based on user's screen settings
	 *
	 * initial size will be smaller than the dimensions of the user's screen once
	 * sized, the window is maximized to fill the screen
	 */
	private void resizeGUI() {
		// set window size
		if (screenWidth >= 1280)
			setSize(1024, 768);
		else if (screenWidth >= 1024)
			setSize(800, 600);
		else if (screenWidth >= 800)
			setSize(640, 480);

		// maximize window
		setExtendedState(this.getExtendedState() | this.MAXIMIZED_BOTH);
	}

	/**
	 * set the current simulation time
	 *
	 * @param time String indicating simulation time in terms of days and turns
	 */
	public void setTime(String time) {
		controlPanel.setTime(time);
	}

	/**
	 * add an event listener to this view
	 *
	 * @param listener listener interested in this view's events
	 */
	public void addSimulationEventListener(SimulationEventListener listener) {
		simulationEventListenerList.add(listener);
	}

	/**
	 * remove an event listener from this view
	 *
	 * @param listener listener to be removed
	 */
	public void removeSimulationEventListener(SimulationEventListener listener) {
		simulationEventListenerList.remove(listener);
	}

	/**
	 * fire a simulation event
	 *
	 * @param eventType the type of event that occurred (see the SimulationEvent
	 *                  class for allowable types)
	 */
	private void fireSimulationEvent(int eventType) {
		// create event
		SimulationEvent simEvent = new SimulationEvent(this, eventType);
		System.out.println("eventtype:" + eventType);
		// inform all listeners
		for (Iterator itr = simulationEventListenerList.iterator(); itr.hasNext();) {
			((SimulationEventListener) itr.next()).simulationEventOccurred(simEvent);
		}

	}

	/**
	 * inner class ControlPanel
	 *
	 * contains buttons for controlling the simulation, and displays the simulation
	 * time
	 */
	private class ControlPanel extends JPanel {

		// button for running the simulation continuously
		private JButton runButton;

		// label for displaying the time in the simulation
		private JLabel timeLabel;

		// event handler for button press events
		private ButtonHandler buttonHandler;

		/**
		 * create a new control panel
		 */
		public ControlPanel() {
			// call superclass constructor
			super();

			// create handler for button press events
			buttonHandler = new ButtonHandler();

			// initialize child components
			initComponents();

			// position child components
			layoutComponents();
		}

		/**
		 * create child components
		 */
		private void initComponents() {

			// button for running simulation continuously
			runButton = new JButton("Run");
			runButton.addActionListener(buttonHandler);
			runButton.setToolTipText("Run the simulation continuously");

			// label for displaying simulation time
			timeLabel = new JLabel();
			timeLabel.setFont(new Font("Verdana", Font.BOLD, 12));
		}

		/**
		 * position child components and add them to this view
		 */
		private void layoutComponents() {
			this.add(runButton);
			this.add(timeLabel);
		}

		/**
		 * set the current simulation time
		 *
		 * @param time String indicating simulation time in terms of days and turns
		 */
		public void setTime(String time) {
			timeLabel.setText("     " + time);
		}

		/**
		 * inner class ButtonHandler
		 *
		 * responsible for handling button press events from the control panel
		 */
		private class ButtonHandler implements ActionListener {

			/**
			 * respond to a button action fires a simulation event appropriate for the
			 * button that is pressed
			 */
			public void actionPerformed(ActionEvent e) {
				// get the button that was pressed
				JButton b = (JButton) e.getSource();
				// fire appropriate event
				if (b.getText().equals("Run")) {
					// run the simulation continuously
					fireSimulationEvent(SimulationEvent.RUN_EVENT);
				} else if (b.getText().equals("Step")) {
					// run the simulation one turn at a time
					fireSimulationEvent(SimulationEvent.STEP_EVENT);
				}
			}
		}
	}

	public AgentView getColonyView() {
		return colonyView;
	}

	public void setColonyView(AgentView colonyView) {
		this.colonyView = colonyView;
	}

	public JScrollPane getColonyPane() {
		return colonyPane;
	}

	public void setColonyPane(JScrollPane colonyPane) {
		this.colonyPane = colonyPane;
	}

	public ControlPanel getControlPanel() {
		return controlPanel;
	}

	public void setControlPanel(ControlPanel controlPanel) {
		this.controlPanel = controlPanel;
	}

	public void startUI() {
		Thread agent = new Thread(agentdisplay);
		agent.start();
	//	Timer timer = new Timer();
	//	timer.schedule(new ShowTime(), new Date(), 10);
	//	Timer timer1 = new Timer();
	//	timer1.schedule(new ProduceTime(), new Date(), 2000);
	}



//	class ShowTime extends TimerTask {
//		public void run() {
//			agentdisplay.run();
//		}
//	}
//


}