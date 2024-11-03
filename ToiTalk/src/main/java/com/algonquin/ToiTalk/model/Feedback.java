package com.algonquin.ToiTalk.model;

import java.sql.Timestamp;

public class Feedback {

	private int feedbackID;
	private int bookingID;
	private int tutorID;
	private String tutorName;
	private int sessionID;
	private int rating;
	private String comment;
	private Timestamp created;
	
	
	public Feedback(int feedbackID, int bookingID, int tutorID, String tutorName
			, int rating, String comment, Timestamp created) {
		this.feedbackID = feedbackID;
		this.bookingID = bookingID;
		this.tutorID = tutorID;
		this.tutorName = tutorName;
		this.rating = rating;
		this.comment = comment;
		this.created = created;
	}
	
	//getter and setter
	public int getFeedbackID() {
		return feedbackID;
	}
	public void setFeedbackID(int feedbackID) {
		this.feedbackID = feedbackID;
	}
	public int getBookingID() {
		return bookingID;
	}
	public void setBookingID(int bookingID) {
		this.bookingID = bookingID;
	}
	public String getTutorName() {
		return tutorName;
	}
	public void setTutorName(String tutorName) {
		this.tutorName = tutorName;
	}
	public int getTutorID() {
		return tutorID;
	}
	public void setTutorID(int tutorID) {
		this.tutorID = tutorID;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Timestamp getCreated() {
		return created;
	}
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	
	
}
