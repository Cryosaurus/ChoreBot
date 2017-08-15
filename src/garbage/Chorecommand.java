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
		//Initialize gson object and json reader/writer
		Gson gson = new GsonBuilder().create();
		JsonReader reader;
		Writer writer;
		
		//Initialize array and create new tenant
		ArrayList<Choreperson> tenants = new ArrayList<Choreperson>();
		String name = full.split(" ",3)[1];
		Boolean paid = Boolean.valueOf(full.split(" ",3)[2]);

		try {
			//Open JSON file to retrieve saved tenant list
			reader = new JsonReader(new FileReader("tenants.json"));
			Type collectionType = new TypeToken<ArrayList<Choreperson>>(){}.getType();
			tenants = gson.fromJson(reader, collectionType);
			reader.close();
			
			//Update rent info assuming it can be updated
			if(tenants != null){
				for(int q =0; q < tenants.size(); q++){
					if(name.equalsIgnoreCase(tenants.get(q).getName())){
						tenants.get(q).paidRent(paid);
					}
				}
				
				//Save JSON fil with modified information
				writer = new FileWriter("tenants.json");
				gson.toJson(tenants, writer);
				writer.close();	
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "Updated info for " + name;
	}

	private static String removeTenant(String full) {
		//Initialize gson object and json reader/writer
		Gson gson = new GsonBuilder().create();
		JsonReader reader;
		Writer writer;
		
		//Initialize array and create new tenant
		ArrayList<Choreperson> tenants = new ArrayList<Choreperson>();
		String name = full.split(" ",2)[1];

		try {
			//Open JSON file to retrieve saved tenant list
			reader = new JsonReader(new FileReader("tenants.json"));
			Type collectionType = new TypeToken<ArrayList<Choreperson>>(){}.getType();
			tenants = gson.fromJson(reader, collectionType);
			reader.close();
			
			//Remove the person from JSON file after making sure the file isn't empty
			if(tenants != null){
				for(Choreperson temp: tenants){
					if(name.equalsIgnoreCase(temp.getName())){
						tenants.remove(temp);
					}
				}
				
				//Save JSON fil with modified information
				writer = new FileWriter("tenants.json");
				gson.toJson(tenants, writer);
				writer.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "Removed " + name;
	}

	private static String addTenant(String full) {
		
		//Initialize gson object and json reader/writer
		Gson gson = new GsonBuilder().create();
		JsonReader reader;
		Writer writer;
		
		//Initialize array and create new tenant
		ArrayList<Choreperson> tenants = new ArrayList<Choreperson>();
		String name = full.split(" ",2)[1];
		Choreperson person = new Choreperson(name);

		try {
			//Open JSON file to retrieve saved tenant list
			reader = new JsonReader(new FileReader("tenants.json"));
			Type collectionType = new TypeToken<ArrayList<Choreperson>>(){}.getType();
			tenants = gson.fromJson(reader, collectionType);
			reader.close();
			
			//add the person made earlier to JSON file after making sure the file isn't empty
			if(tenants == null){
				tenants = new ArrayList<Choreperson>();
			}
			tenants.add(person);
			
			//Save JSON fil with modified information
			writer = new FileWriter("tenants.json");
			gson.toJson(tenants, writer);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "Added " + name;
	}
}
