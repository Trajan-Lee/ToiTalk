package com.algonquin.ToiTalk.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.algonquin.ToiTalk.model.Booking;

public class BookingDAO {
	Connection connection;
	
	public BookingDAO(Connection connection) {
		this.connection = connection;
	}
	
	public List<Booking> loadBookings (Integer bookingID, Integer studentID, Integer tutorID){
		List<Booking> bookList = new ArrayList<>();
		
		//TODO test this for wildcare functionality
	    String sql = "SELECT * FROM bookings"
	    		   + "WHERE booking_id = ?"
	    		   + "AND student_id = ?"
	    		   + "AND tutor_id = ?";
	    try {
	    	PreparedStatement statement = connection.prepareStatement(sql);
			//add wildcare to ID if null
			if (bookingID != null) {
				statement.setInt(1, bookingID);
			} else {
				String bID = String.valueOf(bookingID);
				statement.setString(1, bID);
			}
			
			if (studentID != null) {
				statement.setInt(2, studentID);
			} else {
				String sID = String.valueOf(studentID);
				statement.setString(2, sID);
			}
			
			if (tutorID != null) {
				statement.setInt(3, tutorID);
			} else {
				String tID = String.valueOf(tutorID);
				statement.setString(3, tID);
			}

			ResultSet rs = statement.executeQuery();
			UserDAO userDAO = new UserDAO(connection);
			String tutorName;
			String studentName;
			while (rs.next()) {
				tutorName = userDAO.getTutorNameByID(rs.getInt("tutor_id"));
				studentName = userDAO.getStudentNameByID(rs.getInt("student_id"));
				Booking book = new Booking(rs.getInt("booking_id"), rs.getInt("tutor_id"), rs.getInt("student_id")
						, tutorName, studentName, rs.getTimestamp("date"), rs.getString("status"));
				bookList.add(book);
			}
		} catch (SQLException e) {
	    	e.printStackTrace();
			System.out.println("Could not load from SQL: BOOKING DATA");
		}
		
	    return bookList;
	}
}
