package garbage;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public class Chorereminder implements Runnable {

	private ArrayList<Choreperson> tenants;
	
	//This is a bit useless but I need to figure out a better way to access the client so I can send a message outside of the core
	@Override
	public void run() {
		Gson gson = new Gson();
		JsonReader reader;
		
		Calendar cal = Calendar.getInstance();
		int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
		String message = "";
		String names = "";

		//Weekly garbage day warning
		if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY){
			System.out.println("Starting garbage reminder...");
			message  = message + "It's time to take out the garbage.\n";
		}
		
		//Early warning for rent
		if(dayOfMonth == 25){
			System.out.println("Starting early rent warning...");
			message  = message + "Reminder that rent will be due soon.\n";
		}
		
		//Either print first reminder for rent or put together the daily reminder
		if(dayOfMonth == 1){
			System.out.println("First day of month rent reminder...");
			message  = message + "Rent is now due. Daily reminders will be issued until it is collected.\n";
			Chorecommand.resetRent();
		}else{
			try {
				reader = new JsonReader(new FileReader("tenants.json"));
				Type collectionType = new TypeToken<ArrayList<Choreperson>>(){}.getType();
				tenants = gson.fromJson(reader, collectionType);
				reader.close();
				
				if(tenants != null){
					for(Choreperson temp: tenants){
						if(!temp.hasPaid()){
							names = names + temp.getName() + "\n";
						}
					}
				}
				
				if (!names.isEmpty()){
					message = message + "Rent is still owed by the following people for the month of " +
							  cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH) + ":\n" + names;	
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Chorecore.reminder(message);	
	}
}
