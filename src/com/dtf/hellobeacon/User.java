package com.dtf.hellobeacon;


public class User {
	
	private String firstname;
	private String lastname;
	private String email;
	private String gym;
	private int visits;
	private boolean inGym;
	
	
	public User(String firstname, String lastname, String gym, String email, int visits, boolean inGym){
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.gym = gym;
		this.visits = visits;
		this.inGym = inGym;
	}
	
	
	//SETTERS
	public void setFirstName(String newFirstName){
		this.firstname = newFirstName;
	}
	public void setLastName(String newLastName){
		this.lastname = newLastName;
	}
	public void setEmail(String newEmail){
		this.email = newEmail;
	}
	public void setGymID(String newGym){
		this.gym = newGym;
	}
	public void setVisits(int newVisits){
		this.visits = newVisits;
	}
	public void setInGym(boolean isInGym){
		this.inGym = isInGym;
	}
	
	//GETTERS
	public String getFirstName(){
		return this.firstname;
	}
	public String getLastName(){
		return this.lastname;
	}
	public String getEmail(){
		return this.email;
	}
	public String getGym(){
		return this.gym;
	}
	public int getVisits(){
		return this.visits;
	}
	public boolean getInGym(){
		return this.inGym;
	}	
	
	//----
	public void incrementVisits() {
		this.visits += 1;
	}
}
