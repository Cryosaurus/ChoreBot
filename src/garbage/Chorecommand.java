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
 * Static class that contains the command parsing and the relevant actions that match the text command
 */
public class Chorecommand {
	
	private static final long SNOWRAPTOR = 181630948583014400L;
	private static final int MONTHLY_RATE = 500;

	public static String parse(String msg, Long author){
		String reply = msg.substring(0, 1);
		
		if (reply.equals("!")){
			reply = msg.split(" ",2)[0];
			reply = checkList(reply, msg, author);
		}else{
			reply = "";
		}
		
		return reply;
	}
	
	private static String checkList(String check, String full, Long author){
		String reply = "";
		
        switch (check) {
        case "!commands":
        	reply = "!add NAME - adds person to list of tenants for house\n" + 
        		  "!remove NAME - removes person from list of tenants for house\n" + 
        		  "!list - lists the current tenants and indicates whether they have paid or not\n" +
        		  "!rent NAME AMOUNT - Register change of rent balance in the value of AMOUNT. Negative AMOUNT will increase the amount owed. Requires heightened priveledge.";
        	break;
        case "!add":
        	reply = addTenant(full);
        	break;
        case "!remove":
        	reply = removeTenant(full);
        	break;
        case "!rent":
        	if (author == SNOWRAPTOR){
        		reply = processRent(full);
        	}else{
        		reply = "You do have authority to perform that action.";
        	}
        	break;
        case "!chorecorebo":
        	reply = "Congratulations you found a secret command, have a chocobo. http://i.imgur.com/xi8Tvmh.png";
        	break;
        case "!list":
        	reply = listTenants();
        	break;
        case "!reset":
        	reply = resetRent();
        	break;
        default:
        	reply = "";
        	System.out.println("sadness");
        }
		
		return reply;
	}

	public static String resetRent() {
		//Initialize gson object and json reader/writer
		Gson gson = new GsonBuilder().create();
		JsonReader reader;
		Writer writer;
		
		//Initialize array and create new tenant
		ArrayList<Choreperson> tenants = new ArrayList<Choreperson>();

		try {
			//Open JSON file to retrieve saved tenant list
			reader = new JsonReader(new FileReader("tenants.json"));
			Type collectionType = new TypeToken<ArrayList<Choreperson>>(){}.getType();
			tenants = gson.fromJson(reader, collectionType);
			reader.close();
			
			//add the person made earlier to JSON file after making sure the file isn't empty
			if(tenants == null){
				tenants = new ArrayList<Choreperson>();
			}else{
				for(int q = 0; q < tenants.size(); q++){
					tenants.get(q).paidRent(false);
					tenants.get(q).setOwes(tenants.get(q).getOwes() + MONTHLY_RATE);
				}
			}
			
			//Save JSON fil with modified information
			writer = new FileWriter("tenants.json");
			gson.toJson(tenants, writer);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "All tenants marked as owing rent";
	}

	private static String listTenants() {
		Gson gson = new GsonBuilder().create();
		JsonReader reader;
		
		ArrayList<Choreperson> tenants = new ArrayList<Choreperson>();
		String message = "";
		
		try {
			reader = new JsonReader(new FileReader("tenants.json"));
			Type collectionType = new TypeToken<ArrayList<Choreperson>>(){}.getType();
			tenants = gson.fromJson(reader, collectionType);
			reader.close();
			
			if(tenants != null){
				for(Choreperson temp: tenants){
					message = message + temp.getName() + " owes " + temp.getOwes() + "\n";
				}
			}else{
				message = "No tenants found.";
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		return message;
	}

	private static String processRent(String full) {
		//Initialize gson object and json reader/writer
		Gson gson = new GsonBuilder().create();
		JsonReader reader;
		Writer writer;
		
		//Initialize array and create new tenant
		ArrayList<Choreperson> tenants = new ArrayList<Choreperson>();
		String name = full.split(" ",3)[1];
		Integer paid = Integer.valueOf(full.split(" ",3)[2]);
		Integer owes = 0;

		try {
			//Open JSON file to retrieve saved tenant list
			reader = new JsonReader(new FileReader("tenants.json"));
			Type collectionType = new TypeToken<ArrayList<Choreperson>>(){}.getType();
			tenants = gson.fromJson(reader, collectionType);
			reader.close();
			
			//Update rent info assuming it can be updated
			if(tenants != null){
				for(int q =0; q < tenants.size(); q++){
					owes = 0;
					if(name.equalsIgnoreCase(tenants.get(q).getName())){
						owes = tenants.get(q).getOwes() - paid;
						tenants.get(q).setOwes(owes);
						if (owes > 0){
							tenants.get(q).paidRent(false);
						}else{
							tenants.get(q).paidRent(true);
						}
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
						break;
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
