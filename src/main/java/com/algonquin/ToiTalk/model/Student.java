package com.algonquin.ToiTalk.model;

import java.sql.Timestamp;

public class Student extends User {
	
	private int studentID;
	
	public Student(String username, String password, String email
			, String type, int ID, Timestamp create_time, int studentID) {
		super(username, password, email, type, ID, create_time);
		this.studentID = studentID;
	}

	public int getStudentID() {
		return studentID;
	}
	public void setStudentID(int studentID) {
		this.studentID = studentID;
	}
}
