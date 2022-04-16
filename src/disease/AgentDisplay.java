package disease;

import java.util.concurrent.LinkedBlockingDeque;

import DiseaseSim.Agent;
import DiseaseSim.AgentState;
import DiseaseSim.Message;
import DiseaseSim.MessageAction;

public class AgentDisplay implements Runnable {
	LinkedBlockingDeque<Message> linkque;

	public AgentDisplay(LinkedBlockingDeque<Message> linkque) {
		this.linkque = linkque;
	}

	@Override
	public  synchronized void run() {
		try {
			 while(true) {
				if (linkque.size() <= 0) {
					return;
				}
				System.out.println("agentdisplay linkquesize:"+linkque.size());
				
				MessageAction a = (MessageAction) linkque.take();
				System.out.println("agentdisplay linkquesize:"+linkque.size());
				
				Agent b = a.getAgent();
				//a.doAction(b);
				AgentNodeView cnv = new AgentNodeView();
				cnv.setID(b.getXPos() + "," + b.getYPos()+":"+b.getState());
				cnv.showIcon();
				
				if (b.getState() == AgentState.DEAD) {
					cnv.setBackgroundColor(5); // grey
				}
				if (b.getState() == AgentState.IMMUNE) {
					cnv.setBackgroundColor(4); // green
				}
				if (b.getState() == AgentState.INCUBATING) {
					cnv.setBackgroundColor(0); // red
				}
				if (b.getState() == AgentState.SICK) {
					cnv.setBackgroundColor(1); // orange
				}
				if (b.getState() == AgentState.VULNERABLE) {
					cnv.setBackgroundColor(2); // yellow
				}
		
				MainProgram.cv.addColonyNodeView(cnv, b.getXPos(), b.getYPos());
				System.out.println("x:" + b.getXPos() + ",y:" + b.getYPos());
			
				MainProgram.cv.repaint();
			//	b.run();
				Thread.sleep(2000);
			 }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
