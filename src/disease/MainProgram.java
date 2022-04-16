package disease;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingDeque;

import DiseaseSim.Agent;
import DiseaseSim.AgentManager;
import DiseaseSim.AgentState;
import DiseaseSim.BoardType;
import DiseaseSim.ConfigInfo;
import DiseaseSim.Message;
import DiseaseSim.MessageAction;

public class MainProgram {

	public static DiseaseGUI gui;
	public static AgentView cv;
	static int step;
	public static AgentManager agentManager = null;
	public static HashMap<String,Agent> agentMap;
	public static void main(String[] args) {
		 agentMap= new HashMap<String,Agent>();

		gui = new DiseaseGUI();

		ConfigInfo info = new ConfigInfo();
		info.exposureDistance = 10;
		info.dimHeight = 100;
		info.dimWidth = 100;
		info.boardType = BoardType.RANDOM;
		info.numAgents = 50;
		info.initSick = 1;
		agentManager = new AgentManager(info);
		agentManager.initAgents();
		cv = new AgentView(info.dimWidth, info.dimHeight);
		gui.initGUI(cv);
		gui.setVisible(true);
		step=0;
	}

	/**
	 * start into simulation
	 */
	public static void startSimulate() {
		for (Agent a : MainProgram.agentManager.getAgentList()) {
			 Message msg=new MessageAction(a);
			 MainProgram.gui.msg_que.add(msg);
			 Thread athread=new Thread(a);
			 athread.start();
		}
		

		
		gui.startUI();
	}
	private void addMessageQue(Agent agent,LinkedBlockingDeque<Message> messages) {
		
	}
}
