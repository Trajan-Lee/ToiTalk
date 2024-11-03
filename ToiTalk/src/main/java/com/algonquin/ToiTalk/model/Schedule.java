package com.algonquin.ToiTalk.model;

public class Schedule {

	// schedule[day][hour]
	// week starts at monday = 0, hour starts at midnight = 0
	private int tutorID;
	private boolean[][] schedule;
	public Schedule(int ID, boolean [][] schedule) {
		this.tutorID = ID;
		if (schedule != null) {
			this.schedule = schedule;
		} else {
			this.schedule = new boolean[7][24]; //automatically init to false
		}
	}
	
	//get and set
	public int getTutorID() {
		return tutorID;
	}

	public void setTutorID(int tutorID) {
		this.tutorID = tutorID;
	}
	
	public boolean[][] getSchedule() {
		return schedule;
	}
	public void setSchedule(boolean[][] schedule) {
		this.schedule = schedule;
	}

}
