package com.dtf.hellobeacon;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Gym {
	protected HashMap <Integer, List<Visit>> gymVisitsPerHourMap;
	
	//contact info and miscellaneous
	protected int capacity;
	protected String name;
	protected String address;
	protected String city;
	protected String state;
	protected String phoneNumber;
	protected int openHour;
	protected int closeHour;
	
	public Gym() {
		gymVisitsPerHourMap = new HashMap<Integer, List<Visit>>();
	}
	public Gym(GymBuilder gymBuilder) {
		this.name = gymBuilder.name;
		this.address = gymBuilder.address;
		this.city = gymBuilder.city;
		this.state = gymBuilder.state;
		this.phoneNumber = gymBuilder.phoneNumber;
		this.openHour = gymBuilder.openHour;
		this.closeHour = gymBuilder.closeHour;
		this.capacity = gymBuilder.capacity;
	}
	
	public void setHours(int openHour, int closeHour) {
		this.openHour = openHour;
		this.closeHour = closeHour;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	public void setAddress(String address){		
		this.address = address;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public void setState(String state) {
		this.state = state;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public void setOpenHour(int openHour) {
		this.openHour = openHour;
	}
	public void setCloseHour(int closeHour) {
		this.closeHour = closeHour;
	}
	
	
	public String getName() {
		return name;
	}
	public int getCapacity() {
		return capacity;
	}
	public String getAddress(){		
		return address;
	}
	public String getCity() {
		return city;
	}
	public String getState() {
		return state;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public int getOpenHour() {
		return openHour;
	}
	public int getCloseHour() {
		return closeHour;
	}
	
	public boolean isOpen(int currentHour) {
		if(openHour <= currentHour && currentHour < closeHour) {
			return true;
		}
		return false;
	}
	
	public static class GymBuilder {
		Gym gym;
		protected int capacity;
		protected String name;
		protected String address;
		protected String city;
		protected String state;
		protected String phoneNumber;
		protected int openHour;
		protected int closeHour;
		
		public GymBuilder() {
			gym = new Gym();
		}
		
		public GymBuilder buildName(String name) {
			this.gym.setName(name);
			this.name = name;
			return this;
		}
		public GymBuilder buildCapacity(int capacity) {
			this.capacity = capacity;
			this.gym.setCapacity(capacity);
			return this;
		}
		public GymBuilder buildPhone(String phoneNumber) {
			this.phoneNumber = phoneNumber;
			this.gym.setPhoneNumber(phoneNumber);
			return this;
		}
		public GymBuilder buildAddress(String address, String city, String state) {
			this.address = address;
			this.city = city;
			this.state = state;
			this.gym.setAddress(address);
			this.gym.setCity(city);
			this.gym.setState(state);
			return this;
		}
		
		public GymBuilder buildContactInfo(String address, String city, String state, String phoneNumber) {
			this.address = address;
			this.city = city;
			this.state = state;
			this.phoneNumber = phoneNumber;
			this.gym.setAddress(address);
			this.gym.setCity(city);
			this.gym.setState(state);
			this.gym.setPhoneNumber(phoneNumber);
			return this;
		}
		
		public GymBuilder buildHours(int openHour, int closeHour) {
			this.openHour = openHour;
			this.closeHour = closeHour;
			this.gym.setOpenHour(openHour);
			this.gym.setCloseHour(closeHour);
			return this;
		}
		
		public Gym build() {
			gym = new Gym(this);
			return gym;
		}
	}
}
