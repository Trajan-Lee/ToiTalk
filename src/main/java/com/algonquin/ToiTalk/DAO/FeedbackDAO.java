package com.algonquin.ToiTalk.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.algonquin.ToiTalk.model.Feedback;

public class FeedbackDAO {

	Connection connection;
	public FeedbackDAO(Connection connection) {
		this.connection = connection;
	}
	
	public List<Feedback> loadFeedback(Integer feedbackID, Integer bookingID, Integer tutorID){
		List<Feedback> fbList = new ArrayList<>();
		String sql = "SELECT * FROM feedback "
				+ "WHERE feedback_id LIKE ? "
				+ "AND booking_id LIKE ? "
				+ "AND tutor_id LIKE ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)){
			//add wildcare to ID if null
			if (feedbackID != null) {
				statement.setInt(1, feedbackID);
			} else {
				statement.setString(1, "%");
			}
			
			if (bookingID != null) {
				statement.setInt(2, bookingID);
			} else {
				statement.setString(2, "%");
			}
			
			if (tutorID != null) {
				statement.setInt(3, tutorID);
			} else {
				statement.setString(3, "%");
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
	
    public Feedback addFeedback(Feedback feedback) {
        String sql = "INSERT INTO feedback (booking_id, rating, comment, tutor_id) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, feedback.getBookingID());
            statement.setInt(2, feedback.getRating());
            statement.setString(3, feedback.getComment());
            statement.setInt(4, feedback.getTutorID());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                // Retrieve the generated keys
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        // Get the auto-incremented feedback_id
                        feedback.setFeedbackID(generatedKeys.getInt(1));
                    } else {
                        System.err.println("No feedback ID obtained.");
                    }
                }
            }
            return feedback;
        } catch (SQLException e) {
            System.err.println("Failed to add feedback: " + e.getMessage());
            return feedback;
        }
    }
    
    // Method to update feedback
    public boolean updateFeedback(Feedback feedback) {
        String sql = "UPDATE feedback SET booking_id = ?, rating = ?, comment = ?, tutor_id = ? WHERE feedback_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, feedback.getBookingID());
            statement.setInt(2, feedback.getRating());
            statement.setString(3, feedback.getComment());
            statement.setInt(4, feedback.getTutorID());
            statement.setInt(5, feedback.getFeedbackID()); // The primary key used for updating

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Failed to update feedback: " + e.getMessage());
            return false;
        }
    }

    // Method to delete feedback
    public boolean deleteFeedback(int feedbackId) {
        String sql = "DELETE FROM feedback WHERE feedback_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, feedbackId);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Failed to delete feedback: " + e.getMessage());
            return false;
        }
    }
	
}
