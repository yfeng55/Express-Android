package com.dtf.hellobeacon;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Gym {
	protected HashMap <Date, List<Visit>> gymVisitsPerHourMap;
	
	//contact info and miscellaneous
	protected int capacity;
	protected String name;
	protected String address;
	protected String city;
	protected String state;
	protected String phoneNumber;
	
	public Gym() {
		gymVisitsPerHourMap = new HashMap<Date, List<Visit>>();
	}
}
