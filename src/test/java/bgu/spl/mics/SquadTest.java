package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.Squad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SquadTest {
    @BeforeEach
    public void setUp(){
    }
    @Test
    public void getInstanceTest1(){
        Squad squad = Squad.getInstance();
        assertNotNull(squad);
    }
    @Test
    public void getInstanceTest2(){
        Squad sq1 = Squad.getInstance();
        Squad sq2 = Squad.getInstance();
        assertEquals(sq1,sq2);
    }

    @Test
    public void laodTest(){
        Squad squad = Squad.getInstance();
        Agent a1 = new Agent("007","elad");
        Agent a2 = new Agent("008","jone");
        Agent a3 = new Agent("009","jack");
        Agent[] agents = {a1,a2,a3};
        squad.load(agents);
        List list = new LinkedList<String>();
        list.add("007");
        list.add("008");
        boolean loadAndGetAgentsWorks =squad.getAgents(list);
        assertTrue(loadAndGetAgentsWorks);
    }
    @Test
    public void releaseTest(){
        Squad squad = Squad.getInstance();
        List list = new LinkedList<String>();
        list.add("007");
        list.add("008");
        squad.releaseAgents(list);
        assertTrue(squad.getAgents(list));
    }

    @Test
    public void getAgentsNamesTest(){
        Squad squad = Squad.getInstance();
        List list = new LinkedList<String>();
        list.add("007");
        list.add("008");
        List agentsNames = squad.getAgentsNames(list);
        List names = new LinkedList<String>();
        names.add("elad");
        names.add("jone");
        assertEquals(agentsNames,names);
    }
}
