package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import java.util.List;

public class MissionAbortedEvent implements Event<Boolean> {
    private List<String> serials;
    public MissionAbortedEvent(List<String> serials){
        this.serials = serials;
    }
    public List<String> getAgents(){
        return serials;
    }
}
