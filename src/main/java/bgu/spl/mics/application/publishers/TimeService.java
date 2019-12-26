package bgu.spl.mics.application.publishers;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.Publisher;
import bgu.spl.mics.application.messages.SystemSDBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;

/**
 * TimeService is the global system timer There is only one instance of this Publisher.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other subscribers about the current time tick using {@link TickBroadcast}.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends Publisher {

	private int terminateTime;
	private final int TICK = 100;
	private int runningTime;


	public TimeService(int terminateTime) {
		super("TimeService");
		this.terminateTime = terminateTime;
		runningTime = 1;
	}

	@Override
	protected void initialize() {
	}

	@Override
	public void run() {
		initialize();
		while(runningTime <= terminateTime){
			Broadcast tickBroadcast = new TickBroadcast(runningTime);
			getSimplePublisher().sendBroadcast(tickBroadcast);
			try { Thread.sleep(TICK); } catch (InterruptedException e) {}
			runningTime++;
		}
		Broadcast systemShutdown = new SystemSDBroadcast();
		getSimplePublisher().sendBroadcast(systemShutdown);

	}

}
