package com.algonquin.ToiTalk.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.algonquin.ToiTalk.model.Booking;
import com.algonquin.ToiTalk.model.Feedback;

public class FeedbackDAO {

	Connection connection;
	public FeedbackDAO(Connection connection) {
		this.connection = connection;
	}
	
	public List<Feedback> loadFeedback(Integer feedbackID, Integer bookingID, Integer tutorID){
		List<Feedback> fbList = new ArrayList<>();
	    String sql = "SELECT * FROM bookings"
	    		   + "WHERE feedback_id = ?"
	    		   + "AND booking_id = ?"
	    		   + "AND tutor_id = ?";
	    try {
	    	PreparedStatement statement = connection.prepareStatement(sql);
			//add wildcare to ID if null
			if (feedbackID != null) {
				statement.setInt(1, feedbackID);
			} else {
				String fID = String.valueOf(feedbackID);
				statement.setString(1, fID);
			}
			
			if (bookingID != null) {
				statement.setInt(2, bookingID);
			} else {
				String bID = String.valueOf(bookingID);
				statement.setString(2, bID);
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
			while (rs.next()) {
				tutorName = userDAO.getTutorNameByID(rs.getInt("tutor_id"));
				Feedback fb = new Feedback(rs.getInt("feedback_id"), rs.getInt("booking_id"), rs.getInt("tutor_id")
						, tutorName, rs.getInt("rating"), rs.getString("comment"), rs.getTimestamp("created_at"));
				fbList.add(fb);
			}
		} catch (SQLException e) {
	    	e.printStackTrace();
			System.out.println("Could not load from SQL: BOOKING DATA");
		}
		
	    return fbList;
		
	}
}
