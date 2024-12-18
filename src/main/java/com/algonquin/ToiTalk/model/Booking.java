
package com.algonquin.ToiTalk.model;

import java.sql.Timestamp;

public class Booking {
    private int bookingID;
    private int tutorID;
    private int studentID;
    private String tutorName;
    private String studentName;
    private Timestamp date;
    private String status;
    private boolean feedback;

    public Booking(int bookingID, int tutorID, int studentID, String tutorName, 
    		String studentName, Timestamp date, String status, boolean feedback) {
        this.bookingID = bookingID;
        this.tutorID = tutorID;
        this.studentID = studentID;
        this.tutorName = tutorName;
        this.studentName = studentName;
        this.date = date;
        this.status = status;
        this.feedback = feedback;
    }

    // Getters and setters
    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    public int getTutorID() {
        return tutorID;
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

	public boolean getFeedback() {
		return feedback;
	}

	public void setFeedback(boolean feedback) {
		this.feedback = feedback;
	}
}
