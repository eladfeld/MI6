package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.passiveObjects.Report;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {
	private int id;
	int time;
	Diary diary;
	static AtomicInteger nextID = new AtomicInteger(0);


	public M() {
		super("M" + nextID.incrementAndGet());
		diary = Diary.getInstance();
		id = nextID.get();
	}

	@Override
	protected void initialize() {
		subscribeEvent(MissionReceivedEvent.class,(event)->complete(event,initializeMission(event.getMission())));
		subscribeBroadcast(TickBroadcast.class,(event)->this.time = event.getTime());
		subscribeBroadcast(SystemSDBroadcast.class,(event)->terminate());
		
	}

	private Boolean initializeMission(MissionInfo mission) {
		diary.incrementTotal();
		Event ageCheck = new AgentsAvailableEvent(mission.getSerialAgentsNumbers());
		Future<Integer> agentsCheck = getSimplePublisher().sendEvent(ageCheck);
		if (agentsCheck.get() != -1) {
			Event gadCheck = new GadgetAvailableEvent(mission.getGadget());
			Future<Pair<Boolean,Integer>> gadgetCheck = getSimplePublisher().sendEvent(gadCheck);
			Pair<Boolean,Integer> qPair= gadgetCheck.get();
			boolean gadgetTaken = qPair.getFirst();
			int qtime = qPair.getSecound();
			if (gadgetTaken) {
				if (time <= mission.getTimeExpired()) {
					Event ExecuteMission = new MissionExecutedEvent(mission.getSerialAgentsNumbers(), mission.getDuration());
					Future <List<String>> pairFuture = getSimplePublisher().sendEvent(ExecuteMission);
					List<String> agentsName = pairFuture.get();
					List<String> SerialAgentsNumbers = mission.getSerialAgentsNumbers();
					int monneypennyNum = agentsCheck.get();
					String missionName = mission.getMissionName();
					String gadget = mission.getGadget();
					int timeIssued = mission.getTimeIssued();
					Report report = new Report(missionName, id, monneypennyNum, SerialAgentsNumbers
							, agentsName, gadget, qtime, time, timeIssued);
					diary.addReport(report);
					System.out.println(missionName + " succeed by " + getName());
					return true;
				}
			}
		}
		Event abortMission = new MissionAbortedEvent(mission.getSerialAgentsNumbers());
		getSimplePublisher().sendEvent(abortMission);
		System.out.println(mission.getMissionName() + " aborted");
		return false;
	}

}
