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
	
	private static Boolean hourlyEnabled;
    private static final Chorecommand INSTANCE = new Chorecommand();

    private Chorecommand() {
    	hourlyEnabled = false;
    }

    public static Chorecommand getInstance() {
        return INSTANCE;
    }
	
	//These could probably use some better names but I leave that for me to figure out at 3AM one night instead of sleep
	public static void enableHourly(Boolean enableHourlyReminders){
		hourlyEnabled = enableHourlyReminders;
	}
	
	public static Boolean hourlyEnabled(){
		return hourlyEnabled;
	}

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
        	reply = "!garbage - Tells chorebot to stop issuing garbage reminders on Thursday.\n" +
        	      "!add NAME USERNAME ID - adds person to list of tenants for house. All information is required.\n" + 
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
        case "!sandwhich":
        	reply = ":hamburger:";
        	break;
        case "!chorecorebo":
        	reply = "Congratulations you found a secret command, have a chocobo. http://i.imgur.com/xi8Tvmh.png";
        	break;
        case "!list":
        	reply = listTenants();
        	break;
        case "!addmonth":
        	reply = addMonthOfRent();
        	break;
        case "!garbage":
        	if (hourlyEnabled){
        		reply = "Thanks you for taking out the garbage."; //its about time
        		enableHourly(false);
        	}else{
        		reply = "I applaud your enthusiasm but either its not time or someone already did it. Or I'm confused, that could also be it";
        	}
        	break;
        default:
        	reply = "";
        	System.out.println("sadness");
        }
		
		return reply;
	}

	public static String addMonthOfRent() {
		
		ArrayList<Choreperson> tenants =  getTenantsFromJSON();
		
		if(tenants == null){
			//Found an empty file so add a template person
			tenants = new ArrayList<Choreperson>();
		}else{
			for(int q = 0; q < tenants.size(); q++){
				tenants.get(q).paidRent(false);
				tenants.get(q).setOwes(tenants.get(q).getOwes() + MONTHLY_RATE);
			}
		}
		
		saveTenantsToJSON(tenants);
		
		return "All tenants marked as owing rent";
	}

	private static String listTenants() {
		
		ArrayList<Choreperson> tenants = getTenantsFromJSON();
		String message = "";
			
		if(tenants != null){
			for(Choreperson temp: tenants){
				message = message + temp.getName() + " owes " + temp.getOwes() + "\n";
			}
		}else{
			message = "No tenants found.";
		}

		return message;
	}

	private static String processRent(String full) {
		
		ArrayList<Choreperson> tenants = getTenantsFromJSON();
		String message = "";
		String name = "";
		Integer paid = 0;
		Integer owes = 0;
		
		//Parse arguements and try to determine who has paid
		try{
			String fullSplit[] = full.split(" ",3);
			name = fullSplit[1];
			paid = Integer.valueOf(fullSplit[2]);
		} catch (IndexOutOfBoundsException e){
			e.printStackTrace();
			return "Too few arguements for !rent command";
		}
			
		if(tenants == null){
			message = "Cannot process rent, no tenants found";
		}else{
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
			
			message = "Updated info for " + name;
			
			saveTenantsToJSON(tenants);
		}
				
		
		return message;
	}

	private static String removeTenant(String full) {
		
		ArrayList<Choreperson> tenants = getTenantsFromJSON();
		String message = "";
		String name = "";
		
		//Attempt to parse command for relevant info
		try{
			name = full.split(" ",2)[1];
		} catch (IndexOutOfBoundsException e){
			e.printStackTrace();
			return "Too few arguements for !remove command";
		}
		
		if(tenants == null){
			message = "Cannot remove tenant, no tenants found";
		}else{
			for(Choreperson temp: tenants){
				if(name.equalsIgnoreCase(temp.getName())){
					tenants.remove(temp);
					message = "Removed " + name;
					break;
				}
			}
		}

		return message;
	}

	private static String addTenant(String full) {
		
		//Initialize array and create new tenant
		ArrayList<Choreperson> tenants = getTenantsFromJSON();
		String[] info;
		Choreperson person;
		
		//Parse arguements and try to create a new person
		try{
			info = full.split(" ",4); // !add NAME USERNAME ID
			person = new Choreperson(info[1], info[2], Long.parseLong(info[3]));
		} catch (IndexOutOfBoundsException e){
			e.printStackTrace();
			return "Too few arguements for !add command";
		}
		
		//add the person made earlier to JSON file after making sure the file isn't empty
		if(tenants == null){
			tenants = new ArrayList<Choreperson>();
		}
		tenants.add(person);
		
		saveTenantsToJSON(tenants);
		
		return "Added " + info[1];
	}
	
	private static ArrayList<Choreperson> getTenantsFromJSON(){
		Gson gson = new GsonBuilder().create();
		JsonReader reader;
		ArrayList<Choreperson> tenants = new ArrayList<Choreperson>();
		
		try{
			//Open JSON file to retrieve saved tenant list
			reader = new JsonReader(new FileReader("tenants.json"));
			Type collectionType = new TypeToken<ArrayList<Choreperson>>(){}.getType();
			tenants = gson.fromJson(reader, collectionType);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return tenants;
	}
	
	private static void saveTenantsToJSON(ArrayList<Choreperson> tenants){
		Gson gson = new GsonBuilder().create();
		Writer writer;
		
		try{
			writer = new FileWriter("tenants.json");
			gson.toJson(tenants, writer);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
