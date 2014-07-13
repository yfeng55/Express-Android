package com.dtf.hellobeacon;


public class User {
	
	private String firstname;
	private String lastname;
	private String email;
	private String gym;
	
	
	public User(String firstname, String lastname, String gym, String email){
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.gym = gym;
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
	
	
}
