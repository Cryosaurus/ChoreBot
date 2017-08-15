package garbage;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

/*
 * In a proper OOP deign this should not exist
 * It's really nice C code that I made to keep clutter out of the core
 * Future refactor will hopefully find a neater implementation but
 * for now it works and this is my primary concern.
 */
public class Chorecommand {

	public static String parse(String msg){
		String reply = msg.substring(0, 1);
		
		if (reply.equals("!")){
			reply = msg.split(" ",2)[0];
			reply = checkList(reply, msg);
		}else{
			reply = "";
		}
		
		return reply;
	}
	
	private static String checkList(String check, String full){
		String reply = "";
		
        switch (check) {
        case "!commands":
        	reply = "!add NAME - adds person to list of tenants for house\n" + 
        		  "!remove NAME - removes person from list of tenants for house\n" + 
        		  "!rent NAME BOOL -Sets whether or not NAMe has paid rent for the month based on the value of BOOL. Requires manager privelege.";
        	System.out.println("yay");
        	break;
        case "!add":
        	reply = addTenant(full);
        	break;
        case "!remove":
        	reply = removeTenant(full);
        	break;
        case "!rent":
        	reply = processRent(full);
        	break;
        case "!chorecorebo":
        	reply = "Congratulations you found a secret command, have a chocobo. http://i.imgur.com/xi8Tvmh.png";
        	break;
        default:
        	reply = "";
        	System.out.println("sadness");
        }
		
		return reply;
	}

	private static String processRent(String full) {
		Gson gson = new GsonBuilder().create();
		ArrayList<Choreperson> ban = new ArrayList<Choreperson>();
		JsonReader reader;
		Writer writer;
		try {
			reader = new JsonReader(new FileReader("tenants.json"));
			Type collectionType = new TypeToken<ArrayList<Choreperson>>(){}.getType();
			ban = gson.fromJson(reader, collectionType);
			reader.close();
			
			//TODO STUFF
			
			writer = new FileWriter("tenants.json");
			gson.toJson(ban, writer);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "thing";
	}

	private static String removeTenant(String full) {
		Gson gson = new GsonBuilder().create();
		ArrayList<Choreperson> ban = new ArrayList<Choreperson>();
		JsonReader reader;
		Writer writer;
		try {
			reader = new JsonReader(new FileReader("tenants.json"));
			Type collectionType = new TypeToken<ArrayList<Choreperson>>(){}.getType();
			ban = gson.fromJson(reader, collectionType);
			reader.close();
			
			//TODO STUFF
			
			writer = new FileWriter("tenants.json");
			gson.toJson(ban, writer);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "thing";
	}

	private static String addTenant(String full) {
		Gson gson = new GsonBuilder().create();
		ArrayList<Choreperson> ban = new ArrayList<Choreperson>();
		JsonReader reader;
		Writer writer;
		try {
			reader = new JsonReader(new FileReader("tenants.json"));
			Type collectionType = new TypeToken<ArrayList<Choreperson>>(){}.getType();
			ban = gson.fromJson(reader, collectionType);
			reader.close();
			
			//TODO STUFF
			
			writer = new FileWriter("tenants.json");
			gson.toJson(ban, writer);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "thing";
	}
}
