package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Pair;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Inventory;

/**
 * Q is the only Subscriber\Publisher that has access to the {@link bgu.spl.mics.application.passiveObjects.Inventory}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Q extends Subscriber {
	int time;
	Inventory inventory;

	public Q() {
		super("Q");
		time = 0;
		inventory = Inventory.getInstance();
	}

	@Override
	protected void initialize() {
		subscribeEvent(GadgetAvailableEvent.class,(event)->complete(event,getItem(event.getGadget())));
		subscribeBroadcast(TickBroadcast.class,(event)->this.time = event.getTime());
		subscribeBroadcast(SystemSDBroadcast.class,(event)->terminate());
	}
	private Pair<Boolean,Integer> getItem(String gadget){
		return new Pair<>(inventory.getItem(gadget),time);
	}
}
