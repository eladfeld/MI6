package bgu.spl.mics;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;

public class JsonWriter {
    public static void writeToFile(String fileName, Serializable seri )throws IOException{
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FileWriter writer = null;
        try {
            writer = new FileWriter(fileName);
            gson.toJson(seri ,writer );
            writer.flush();
            writer.close();
        } catch (IOException e) {
        }
        finally {
            writer.close();
        }
    }
}
