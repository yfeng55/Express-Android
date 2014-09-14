package com.dtf.hellobeacon.model;

import java.util.ArrayList;


public class User {
	
	private String firstname;
	private String lastname;
	private String email;
	private String gym;
	private boolean inGym;
	private ArrayList<Visit> visits;
	
	
	public User(String firstname, String lastname, String gym, String email, boolean inGym){
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.gym = gym;
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
	public void addVisit(Visit newVisit){
		this.visits.add(newVisit);
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
	public ArrayList<Visit> getVisits(){
		return this.visits;
	}
	public boolean getInGym(){
		return this.inGym;
	}	
		

}
