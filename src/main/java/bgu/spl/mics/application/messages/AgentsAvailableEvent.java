package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import java.util.Collections;
import java.util.List;

public class AgentsAvailableEvent implements Event<Integer> {

    private List<String> serials;

    public AgentsAvailableEvent(List<String> serials){
        this.serials = serials;
        Collections.sort(serials);
    }
    public List<String> getAgents(){
        return serials;
    }
}
