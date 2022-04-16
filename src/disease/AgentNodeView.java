package disease;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.border.BevelBorder;

/**
 * class ColonyNodeView
 *
 * provides a graphical view for a single node in the ant colony
 */
public class AgentNodeView extends JPanel {
	/************
	 * constants
	 ***********/
	// default font for text
	private final Font NODE_FONT = new Font("Verdana", Font.BOLD, 10);
	// background color if node has been revealed
	private final Color OPEN_NODE_COLOR = new Color(200, 178, 55);
	private final Color LABEL_COLOR = Color.BLACK;
	private final Color COLOR_RED = new Color(220, 79, 79); // red
	private final Color COLOR_ORANGE = new Color(255, 204, 50); // orange
	private final Color COLOR_YELLOW = new Color(255, 255, 101); // yellow
	private final Color COLOR_GREEN = new Color(105, 171, 100); // green
	private final Color COLOR_BLUE = new Color(79, 79, 252); // blue
	private final Color COLOR_VIOLET = new Color(169, 78, 202); // violet

	/*************
	 * attributes
	 ************/

	// layout for positioning components
	private SpringLayout layout;

	// displays node ID
	private JLabel IDLabel;

	// icon to indicate presence of queen ant
	private JLabel queenIcon;

	private int xpoint;

	private int ypoint;

	/***************
	 * constructors
	 **************/

	/**
	 * create a new ColonyNodeView
	 */
	public AgentNodeView() {
		super();
		// create and set layout for child components
		layout = new SpringLayout();
		this.setLayout(layout);
		// initialize child components
		initComponents();
		// set background color
		setBackground(OPEN_NODE_COLOR);
		// set border
		setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		// set size
		setPreferredSize(new Dimension(AgentView.NODE_SIZE, AgentView.NODE_SIZE));
	}

	/**********
	 * methods
	 *********/

	/**
	 * initialize child components
	 */
	private void initComponents() {
		// displays ID of this view
		IDLabel = new JLabel("");
		IDLabel.setFont(NODE_FONT);
		IDLabel.setForeground(LABEL_COLOR);

		// indicates presence of queen ant
		queenIcon = new JLabel(new ImageIcon(getClass().getResource("../images/disease1.png")));
		queenIcon.setVisible(true);

		// ID label
		layout.putConstraint(SpringLayout.WEST, IDLabel, 5, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, IDLabel, 5, SpringLayout.NORTH, this);

		// queen icon
		layout.putConstraint(SpringLayout.WEST, queenIcon, 10, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, queenIcon, 20, SpringLayout.NORTH, this);

		// add to view
		this.add(IDLabel);

		this.add(queenIcon);
	}

	/**
	 * make this view visible
	 *
	 * use this method to reveal the contents of the node represented by this view
	 */
	public void showNode() {
		setVisible(true);
	}

	/**
	 * hide this view
	 *
	 * this method is used to hide the contents of the node represented by this view
	 * should only be used during initialization of the simulation
	 */
	public void hideNode() {
		setVisible(false);
	}

	/**
	 * set the value for the ID label
	 *
	 * @param id String that uniquely identifies the node represented by this view
	 */
	public void setID(String id) {
		IDLabel.setText(id);
	}

	public String getID() {
		return IDLabel.getText();
	}

	public void setBackgroundColor(int type) {

		if (type == 1)
			setBackground(COLOR_ORANGE);
		else if (type == 2)
			setBackground(COLOR_YELLOW);
		else if (type == 3)
			setBackground(COLOR_BLUE);
		else if (type == 4)
			setBackground(COLOR_GREEN);
		else if (type == 5)
			setBackground(COLOR_VIOLET);
		else if (type == 0)
			setBackground(COLOR_RED);
		else
			setBackground(OPEN_NODE_COLOR);
	}

	/**
	 * display the queen icon to indicate presence of queen ant
	 */
	public void showIcon() {
		queenIcon.setVisible(true);
	}

	/**
	 * hide the queen icon to indicate no queen ant present
	 */
	public void hideIcon() {
		queenIcon.setVisible(false);
	}

	public int getXpoint() {
		return xpoint;
	}

	public void setXpoint(int xpoint) {
		this.xpoint = xpoint;
	}

	public int getYpoint() {
		return ypoint;
	}

	public void setYpoint(int ypoint) {
		this.ypoint = ypoint;
	}

	public static void main(String[] args) {

	}

}