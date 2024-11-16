package com.algonquin.ToiTalk.model;

import java.sql.Timestamp;
public class Booking {
	
	
	//TODO WHEN MAKING NEW BOOKING, CHANGE STATUS IN TUTOR_SCHEDULE
	//TODO FIGURE OUT HOW TO USE TIMESLOT ID
	//TODO next iteration we will implement a table of recurring bookings
	
	
	
	private int bookingID;
	private int tutorID;
	private int studentID;
	private int slotID;
	private String tutorName;
	private String studentName;
	private Timestamp date;
	private String status;
	
	
	public Booking(int bookingID, int tutorID, int studentID, int slotID, String tutorName
			, String studentName, Timestamp date,String status) {
		this.bookingID = bookingID;
		this.tutorID = tutorID;
		this.studentID = studentID;
		this.slotID = slotID;
		this.tutorName = tutorName;
		this.studentName = studentName;
		this.date = date;
		this.status = status;
	}
	
	//set and get
	
	public int getTutorID() {
		return tutorID;
	}
	public int getBookingID() {
		return bookingID;
	}
	public void setBookingID(int bookingID) {
		this.bookingID = bookingID;
	}

	public void setTutorID(int tutorID) {
		this.tutorID = tutorID;
	}
	public int getStudentID() {
		return studentID;
	}
	public void setStudentID(int studentID) {
		this.studentID = studentID;
	}
	public String getTutorName() {
		return tutorName;
	}
	public void setTutorName(String tutorName) {
		this.tutorName = tutorName;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public Timestamp getDate() {
		return date;
	}
	public void setDate(Timestamp date) {
		this.date = date;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public int getSlotID() {
		return slotID;
	}

	public void setSlotID(int scheduleID) {
		this.slotID = scheduleID;
	}
	
	

	
	


}
