package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;

import java.util.List;

public class MissionExecutedEvent implements Event<List<String>> {

    private List<String> serials;
    private int duertion;

    public MissionExecutedEvent(List<String> serials ,int duertion){
        this.serials = serials;
        this.duertion = duertion;
    }

    public List<String> getAgents() {
        return serials;
    }
    public int getDuertion(){
        return duertion;
    }
}
