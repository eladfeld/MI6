package bgu.spl.mics.application.subscribers;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Squad;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Only this type of Subscriber can access the squad.
 * Three are several Moneypenny-instances - each of them holds a unique serial number that will later be printed on the report.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Moneypenny extends Subscriber {
	private Squad squad;
	private int id;
	static AtomicInteger nextID = new AtomicInteger(0);
	private final int MILIS = 100;


	public Moneypenny() {
		super("MonneyPenny" + nextID.incrementAndGet());
		squad = Squad.getInstance();
		id = nextID.get();
	}

	@Override
	protected void initialize() {
		if(id % 2 == 0)subscribeEvent(AgentsAvailableEvent.class,(event)->complete(event,getAgents(event.getAgents())));
		else{
			subscribeEvent(MissionAbortedEvent.class,(event)->complete(event,releaseAgents(event.getAgents())));
			subscribeEvent(MissionExecutedEvent.class, (event)->complete(event,sendAgents(event.getAgents(),event.getDuertion())));
		}
		subscribeBroadcast(SystemSDBroadcast.class,(event)->terminate());
	}

	private Integer getAgents(List<String> agents) {
		if(squad.getAgents(agents)) return id;
		return -1;
	}

	private List<String> sendAgents(List<String> agents , int duertion) {
		squad.sendAgents(agents,duertion * MILIS);
		return squad.getAgentsNames(agents);
	}

	private Boolean releaseAgents(List<String> agents) {
		squad.releaseAgents(agents);
		return true;
	}
}
