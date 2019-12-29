package bgu.spl.mics;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {


	private static class MessageBrokerHolder {
		private static MessageBrokerImpl instance = new MessageBrokerImpl();
	}

	private Map<Class<? extends Message>, Queue<Subscriber>> topicMap;  //this map is mapping between a type of message and the queue of subscribers to this kind of message
	private Map<Event, Future> futureMap;   //this map is mapping between a event and a future related to this event
	private Map<Subscriber, BlockingQueue<Message>> subscribersMap;    //this map is mapping between a subscriber and the queue of messages he not yet accomplished
	private Map<Subscriber, List<Class<? extends Message>>> subscriberTopics;  //this map is mapping between a subscriber and kind of messages it subscribed to (in order to delete this subscriber)


	private MessageBrokerImpl() {
		topicMap = new ConcurrentHashMap<>();
		futureMap = new ConcurrentHashMap<>();
		subscribersMap = new ConcurrentHashMap<>();
		subscriberTopics = new ConcurrentHashMap<>();
	}

	/**
	 * Retrieves the single instance of this class.
	 */
	public static MessageBroker getInstance() {
		return MessageBrokerHolder.instance;
	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {
		topicMap.putIfAbsent(type, new ConcurrentLinkedQueue<>());
		topicMap.get(type).add(m);
		subscriberTopics.get(m).add(type);
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
		topicMap.putIfAbsent(type, new LinkedBlockingQueue<>());
		topicMap.get(type).add(m);
		subscriberTopics.get(m).add(type);
	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		Future futureToResolve = futureMap.get(e);
		if (futureToResolve != null)
			futureToResolve.resolve(result);
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		Queue<Subscriber> queue = topicMap.get(b.getClass());
		if (queue != null)
			for (Subscriber sub : queue)
				subscribersMap.get(sub).add(b);
	}


	@Override
	public synchronized  <T> Future<T> sendEvent(Event<T> e) {
		Queue<Subscriber> queue = topicMap.get(e.getClass());
		if (queue != null) {
			Subscriber sub = queue.poll();
			if(sub == null)
				return null;
			subscribersMap.get(sub).add(e);
			queue.add(sub);
		}
		Future future = new Future();
		futureMap.put(e,future);
		return future;
	}

	@Override
	public void register(Subscriber m) {
		subscribersMap.put(m, new LinkedBlockingQueue<>());
		subscriberTopics.put(m, new CopyOnWriteArrayList<>());
	}

	@Override
	public void unregister(Subscriber m) {
		subscribersMap.remove(m);
		for(Class<? extends Message> message : subscriberTopics.get(m)){
			Queue queue = topicMap.get(message);
			queue.remove(m);
		}
	}

	@Override
	public Message awaitMessage(Subscriber m) throws InterruptedException {
		BlockingQueue<Message> queue = subscribersMap.get(m);
		if(queue != null){
			return queue.take();
		} throw new IllegalStateException();
	}
}
