package garbage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Chorereminder implements Runnable {

	private ArrayList<Choreperson> tenants;
	private Boolean someoneOwesRent;
	
	//This is a bit useless but I need to figure out a better way to access the client so I can send a message outside of the core
	@Override
	public void run() {
		
		Calendar cal = Calendar.getInstance();
		int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
		String message = "";

		//Weekly garbage day warning
		if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY){
			System.out.println("Starting garbage reminder...");
			message  = "It's time to take out the garbage.\n";
		}
		
		//Early warning for rent
		if(dayOfMonth == 25){
			System.out.println("Starting early rent warning...");
			message  = "Reminder that rent will be due soon.\n";
		}
		
		//Either print first reminder for rent or put together the daily reminder
		if(dayOfMonth == 1){
			System.out.println("First day of month rent reminder...");
			message  = "Rent is now due. Daily reminders will be issued until it is collected.\n";
			this.someoneOwesRent = true;
		}else if(this.someoneOwesRent){
			message = "Rent is still owed by the following people for the month of " +
					  cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH) + ":\n";
			for(Choreperson temp: tenants){
				if(!temp.hasPaid()){
					message = message + temp.getName() + "\n";
				}
			}
		}
		
		Chorecore.reminder(message);	
	}
//	TODO figure out where this goes in the new JSON/XML/whatever format being used to exhange data between everything
//	//Add a tenant to the list of people in this household
//	public void addTenant(String name){
//		Choreperson temp = new Choreperson(name);
//		tenants.add(temp);
//	}
//	
//	//Remove a tenant from the list of people in this household
//	public void removeTenant(String name){
//		if (tenants.size() > 0){
//			for(Choreperson temp: tenants){
//				if(name.equals(temp.getName())){
//					tenants.remove(temp);
//				}
//			}
//		}
//	}
//	
//	//Returns a list of tenants living in this house in a format that should fit nicely in discord
//	public String getTenants(){
//		String names = "Tenant list is:\n";
//		if (tenants.size() > 0){
//			for(Choreperson temp:tenants){
//				names = names + temp + "\n"; 
//			}
//		}else{
//			names = names + "EMPTY";
//		}
//		
//		return names;
//	}
//	
//	//Updates the tenant list to reflect that tenant has paid for the month
//	public void registerPayment(String name){
//		
//		Boolean maybeOwes = false;
//		
//		if (tenants.size() > 0){
//			//Find the Choreperson who paid in the tenant list and register payment
//			for(int q = 0; q < tenants.size(); q++){
//				if (tenants.get(q).getName().equals(name)){
//					tenants.get(q).paidRent(true);
//				}
//			}
//			
//			//Check if they are the last Choreperson who has paid rent
//			for(int q = 0; q < tenants.size(); q++){
//				if (tenants.get(q).hasPaid() == false){
//					maybeOwes = true;
//				}
//			}
//			
//			//If they are the last, clear daily rent warning
//			if(!maybeOwes){
//				this.someoneOwesRent = false;
//			}
//		}
//	}
//	
//	public void resetPayments(){
//		if (tenants.size() > 0){
//			for(int q = 0; q < tenants.size(); q++){
//			 tenants.get(q).paidRent(false);
//			}
//		}
//	}
}
