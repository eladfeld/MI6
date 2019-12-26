package bgu.spl.mics.application;
import bgu.spl.mics.MessageBroker;
import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.publishers.*;
import bgu.spl.mics.application.subscribers.*;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Thread.sleep;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {
    public static void main(String[] args) throws Exception {
       // if(args.length < 3)throw new IllegalArgumentException("not enough progrem argumnets");
        List<Thread> threads = createSystem(args[0]);
        for(Thread thread : threads)thread.start();
       // Thread.sleep(2500);




        //after finishing
        for(Thread thread : threads)thread.join();
        Diary diary = Diary.getInstance();
        Inventory inventory = Inventory.getInstance();
        inventory.printToFile(args[1]);
        diary.printToFile(args[2]);


    }

    private static List<Thread> createSystem(String inputFile) {
        List<Thread> threads = new LinkedList<>();
        Gson gson = new Gson();
        JsonReader reader = null;
        try { reader = new JsonReader(new FileReader(inputFile)); }catch (Exception e){}
        GsonInfoCapturer systemInput = gson.fromJson(reader,GsonInfoCapturer.class);
        Inventory inventory = Inventory.getInstance();
        inventory.load(systemInput.inventory);
        Squad squad = Squad.getInstance();
        squad.load(systemInput.squad);
        int numberOfMs = systemInput.services.M;
        int numverOfMoneyPenny = systemInput.services.Moneypenny;
        int terminateTime = systemInput.services.time;
        for(int i = 0; i < numberOfMs; i++) threads.add(new Thread(new M(),"M"));
        for(int i = 0; i < numverOfMoneyPenny; i++) threads.add(new Thread(new Moneypenny(),"Moneypenny"));
        threads.add(new Thread(new Q(), "Q"));
        for(Intelligence intelligence : systemInput.services.intelligence) threads.add(new Thread(intelligence,intelligence.getName()));
        threads.add(new Thread(new TimeService(terminateTime),"TimeService"));
        return threads;
    }
}

