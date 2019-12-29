package bgu.spl.mics.application.passiveObjects;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class. 
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Squad {

	private static class SquadHolder {
		private static Squad instance = new Squad();
	}

	private Map<String, Agent> agents;


	private Squad(){
		agents = new LinkedHashMap<>();
	}
	/**
	 * Retrieves the single instance of this class.
	 */
	public static Squad getInstance() {
		return SquadHolder.instance;
	}

	/**
	 * Initializes the squad. This method adds all the agents to the squad.
	 * <p>
	 * @param agents 	Data structure containing all data necessary for initialization
	 * 						of the squad.
	 */
	public void load (Agent[] agents) {
		for (Agent agent : agents)
			this.agents.put(agent.getSerialNumber(), agent);
	}

	/**
	 * Releases agents.
	 */
	public synchronized void releaseAgents(List<String> serials) {
		Agent a;
		for (String serialNum : serials) {
			a = agents.get(serialNum);
			if(a != null)a.release();
		}
		notifyAll();
	}

	/**
	 * simulates executing a mission by calling sleep.
	 * @param time   time ticks to sleep
	 */
	public void sendAgents(List<String> serials, int time){
		try {
			Thread.currentThread().sleep(time);
		}catch (InterruptedException e){}
		releaseAgents(serials);
	}

	/**
	 * acquires an agent, i.e. holds the agent until the caller is done with it
	 * @param serials   the serial numbers of the agents
	 * @return ‘false’ if an agent of serialNumber ‘serial’ is missing, and ‘true’ otherwise
	 */
	public synchronized boolean getAgents(List<String> serials) {
		for(String serialNum : serials){
			Agent agent = agents.get(serialNum);
			if(agent == null)return false;
			while(!agent.isAvailable())
				try{wait();}catch (InterruptedException e){}
			agent.acquire();
		}
		return true;
	}

    /**
     * gets the agents names
     * @param serials the serial numbers of the agents
     * @return a list of the names of the agents with the specified serials.
     */
    public List<String> getAgentsNames(List<String> serials){
        List<String> names = new LinkedList<>();
        for (String serialNum : serials)
			names.add(agents.get(serialNum).getName());
        return names;
    }

}
