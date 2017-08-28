package garbage;

public class Choreperson{
	private String name;
	private String username;
	private Boolean paid;
	private int owes;
	private long id;
	
	public Choreperson(){
		this.name = "TEMP";
		this.setUsername("TEMP");
		this.paid = false;
		this.owes = 0;
		this.setId(0L);
	}
	
	public Choreperson(String name, String username, long id){
		this.name = name;
		this.setUsername(username);
		this.paid = false;
		this.owes = 0;
		this.setId(id);
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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	@Override
	public String toString(){
		return this.name;
	}
}
