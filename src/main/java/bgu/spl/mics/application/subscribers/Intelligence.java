package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Event;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.messages.SystemSDBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A Publisher\Subscriber.
 * Holds a list of Info objects and sends them
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Intelligence extends Subscriber {

	private List<MissionInfo> missions;
	static AtomicInteger nextID = new AtomicInteger(0);

	public Intelligence() {
		super("Intelligence" + nextID.incrementAndGet());
		missions = new LinkedList<>();
	}

	@Override
	protected void initialize() {
		subscribeBroadcast(TickBroadcast.class,(event)->initializeMissions(event.getTime()));
		subscribeBroadcast(SystemSDBroadcast.class,(event)->terminate());
	}

	private void initializeMissions(int time) {
		for(MissionInfo mission : missions){
			if(time == mission.getTimeIssued()){
				Event sandMission = new MissionReceivedEvent(mission);
				getSimplePublisher().sendEvent(sandMission);
			}
		}
	}
}
