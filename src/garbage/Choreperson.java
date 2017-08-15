package garbage;

public class Choreperson{
	private String name;
	private Boolean paid;
	private int owes;
	
	public Choreperson(){
		this.name = "TEMP";
		this.paid = true;
		this.owes = 0;
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
