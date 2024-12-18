package com.algonquin.ToiTalk.model;

import java.util.List;
import java.sql.Timestamp;



public class Tutor extends User {

	private int tutorID;
	private List<Language> languages;
	private String bio;
	private double rating;
	private int expYears;
	private Schedule schedule;
	
	public Tutor(String username, String password, String email, String type, int ID, Timestamp create_time,
		int tutorID, List<Language> languages, String bio, double rating, int expYears, Schedule schedule) {
		super(username, password, email, type, ID, create_time);
		this.tutorID = tutorID;
		this.languages = languages;
		this.bio = bio;
		this.rating = rating;
		this.expYears = expYears;
		this.schedule = schedule;
	}

	
	//set and gett
	public int getTutorID() {
		return tutorID;
	}
	
	public void setTutorID(int ID) {
		this.tutorID = ID;
	}
	
	public List<Language> getLanguages() {
		return languages;
	}

	public void setLanguages(List<Language> languages) {
		this.languages = languages;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}

	public int getExpYears() {
		return expYears;
	}

	public void setExpYears(int expyears) {
		this.expYears = expyears;
	}

	public Schedule getSchedule() {
		return schedule;
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}
	
}
