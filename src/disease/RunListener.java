package disease;



public class RunListener implements SimulationEventListener {

	public RunListener() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void simulationEventOccurred(SimulationEvent simEvent) {
		// TODO Auto-generated method stub
		if(simEvent.getEventType()==5) {
			MainProgram.startSimulate();
		}
		System.out.println("listener4:"+simEvent.getEventType());
	}

}
