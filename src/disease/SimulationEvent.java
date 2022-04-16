package disease;

import java.util.EventObject;

/**
 *	class SimulationEvent
 *
 *	represents an event that can occur in the simulation
 */
public class SimulationEvent extends EventObject
{
	
	/************
	 *	constants
	 ***********/
	
	// types of events
	
	// set up simulation for normal run
	public final static int NORMAL_SETUP_EVENT = 0;
	

	
	// run simulation continuously
	public final static int RUN_EVENT = 5;
	
	// run simulation one turn at a time
	public final static int STEP_EVENT = 6;
	
	
	/*************
	 *	attributes
	 ************/
	
	// type of event
	private int eventType;
	
	
	/***************
	 *	constructors
	 **************/
	
	/**
	 *	create a new SimulationEvent
	 *
	 *	@param	source		Object on which event occurred
	 *	@param	eventType	type of event
	 */
	public SimulationEvent(Object source, int eventType)
	{
		super(source);
		this.eventType = eventType;
	}
	
	
	/**********
	 *	methods
	 *********/
	
	/**
	 *	return the type of event this is
	 *
	 *	@return	a valid SimulationEvent type
	 */
	public int getEventType()
	{
		return eventType;
	}
}