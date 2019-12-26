package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {
    @BeforeEach
    public void setUp(){
    }

    @Test
    public void getInstanceTest1(){
        Inventory inv = Inventory.getInstance();
        assertNotNull(inv);
    }
    @Test
    public void getInstanceTest2(){
        Inventory inv1 = Inventory.getInstance();
        Inventory inv2 = Inventory.getInstance();
        assertEquals(inv1,inv2);
    }

    @Test
    public void loadAndGetItemTest(){
        Inventory inventory = Inventory.getInstance();
        String[] gadgets = {"rock" , "paper" , "seasers" , "knife" , "gun"};
        inventory.load(gadgets);
        boolean loadWorks = true;
        for(String gadget : gadgets)
            if(!inventory.getItem(gadget))loadWorks = false;
        assertTrue(loadWorks);
    }
    @Test
    public void getItemTest(){
        Inventory inventory = Inventory.getInstance();
        inventory.load(new String[0]);
        assertFalse(inventory.getItem("rock"));
    }
}
