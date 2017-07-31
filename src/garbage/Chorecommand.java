package garbage;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/*
 * In a proper OOP deign this should not exist
 * It's really nice C code that I made to keep clutter out of the core
 * Future refactor will hopefully find a neater implementation but
 * for now it works and this is my primary concern.
 */
public class Chorecommand {

	public static String parse(String msg){
		String com = msg.substring(0, 1);
		if (com.equals("!")){
			com = msg.split(" ",2)[0];
			com = checkList(com);
		}else{
			com = "";
		}
		return com;
	}
	
	private static String checkList(String check){
		String com;
		
        switch (check) {
        case "!commands":
        	com = "I have no other commands at the moment";
        	System.out.println("yay");
        	break;
        default:
        	com = "";
        	System.out.println("sadness");
        }
		
		return com;
	}
}
