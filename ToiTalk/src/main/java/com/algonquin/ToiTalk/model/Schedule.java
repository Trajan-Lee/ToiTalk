package com.algonquin.ToiTalk.model;

public class Schedule {

	// schedule[day][hour]
	// week starts at monday = 0, hour starts at midnight = 0
	private int tutorID;
	private String[][] arrSchedule;
	public Schedule(int ID, String [][] schedule) {
		this.tutorID = ID;
		if (schedule != null) {
			this.arrSchedule = schedule;
		} else {
			this.arrSchedule = new String[7][24]; //automatically init to false
		}
	}
	
	//get and set
	public int getTutorID() {
		return tutorID;
	}

	public void setTutorID(int tutorID) {
		this.tutorID = tutorID;
	}
	
	public String[][] getArrSchedule() {
		return arrSchedule;
	}
	public void setArrSchedule(String[][] schedule) {
		this.arrSchedule = schedule;
	}

}
