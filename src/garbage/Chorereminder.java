package garbage;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public class Chorereminder{
	static class Daily implements Runnable {
		@Override
		public void run() {
			Gson gson = new Gson();
			JsonReader reader;
			ArrayList<Choreperson> tenants = new ArrayList<Choreperson>();
			
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
			int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
			String pubMes = "";
			//Weekly garbage day warning
			if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY){
				System.out.println("Starting garbage reminder...");
				pubMes  = pubMes + "It's time to take out the garbage.\n";
			}
			
			//Early warning for rent
			if(dayOfMonth == 25){
				System.out.println("Starting early rent warning...");
				pubMes  = pubMes + "Reminder that rent will be due soon.\n";
			}
			
			//Either print first reminder for rent or put together the daily reminder
			if(dayOfMonth == 1){
				System.out.println("First day of month rent reminder...");
				pubMes  = pubMes + "Rent is now due. Daily reminders will be issued until it is collected.\n";
				Chorecommand.addMonthOfRent();
			}else{
				try {
					reader = new JsonReader(new FileReader("tenants.json"));
					Type collectionType = new TypeToken<ArrayList<Choreperson>>(){}.getType();
					tenants = gson.fromJson(reader, collectionType);
					reader.close();
					
					if(tenants != null){
						for(Choreperson temp: tenants){
							if(!temp.hasPaid()){
								Chorecore.privateReminder("You still owe " + temp.getOwes() + " in rent", temp.getId());
							}
						}
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			Chorecore.groupReminder(pubMes);	
		}
	}
}
