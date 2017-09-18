package garbage;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public class Chorereminder{
	
	private static int DAYSINWEEK = 7;
	
	static class Hourly implements Runnable{
		@Override
		public void run() {
			Chorecommand.enableHourly(true);
			try {
				TimeUnit.HOURS.sleep(1);
				while(Chorecommand.hourlyEnabled()){
						Chorecore.groupReminder("I won't shut up until someone takes out the garbage.");
						TimeUnit.HOURS.sleep(1);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	static class Daily implements Runnable {
		@Override
		public void run() {
			Gson gson = new Gson();
			JsonReader reader;
			ArrayList<Choreperson> tenants = new ArrayList<Choreperson>();
			
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
			int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
			int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
			String pubMes = "";
			
			Random randomizer = new Random();
			int randint = randomizer.nextInt(DAYSINWEEK) + 1; //Calendar defines days of week in range 1 to 7
			
			//Weekly garbage day warning. Also starts the hourly reminders.
			if(dayOfWeek == Calendar.THURSDAY){
				System.out.println("Starting garbage reminder...");
				pubMes  = pubMes + "It's time to take out the garbage. Hourly reminders will now begin.\n";
				ExecutorService executor = Executors.newFixedThreadPool(1);
				executor.execute(new Hourly());
			}
			
			//Early warning for rent
			if(dayOfMonth == 25){
				System.out.println("Starting early rent warning...");
				pubMes  = pubMes + "Reminder that rent will be due soon.\n";
			}
			
			//Send out rent reminders
			//Per request, rent reminders are no longer daily. I decided that random reminders were more
			//in line with the original goal of annoying people than weekly reminders like roommates want
			//The values might be adjusted if I decide they aren't being pestered enough
			if(dayOfMonth == 1){
				System.out.println("First day of month rent reminder...");
				pubMes  = pubMes + "Rent is now due. Daily reminders will be issued until it is collected.\n";
				Chorecommand.addMonthOfRent();
			}else{
				if (randint == dayOfWeek){
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

			}
			
			Chorecore.groupReminder(pubMes);	
		}
	}
}
