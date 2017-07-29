package garbage;

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
