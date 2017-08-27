package garbage;

public class Choreperson{
	private String name;
	private String username;
	private Boolean paid;
	private int owes;
	private long id;
	
	public Choreperson(){
		this.name = "TEMP";
		this.username = "TEMP";
		this.paid = false;
		this.owes = 0;
		this.id = 0L;
	}
	
	public Choreperson(String name, String username, long id){
		this.name = name;
		this.username = username;
		this.paid = false;
		this.owes = 0;
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean hasPaid() {
		return paid;
	}
	public void paidRent(Boolean paid) {
		this.paid = paid;
	}
	public int getOwes() {
		return owes;
	}
	public void setOwes(int owes) {
		this.owes = owes;
	}
	
	@Override
	public String toString(){
		return this.name;
	}
}
