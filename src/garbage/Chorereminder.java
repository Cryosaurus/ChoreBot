package garbage;

import java.util.Calendar;

public class Chorereminder implements Runnable {

	//This is a bit useless but I need to figure out a better way to access the client so I can send a message outside of the core
	@Override
	public void run() {
		
		int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
		
		System.out.println("Issued reminder... maybe");
		if(day == Calendar.SUNDAY){
			System.out.println("Its time");
			Chorecore.reminder();	
		}
	}
}
