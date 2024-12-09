package com.algonquin.ToiTalk.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.algonquin.ToiTalk.model.Feedback;

public class FeedbackDAO {

	Connection connection;
	public FeedbackDAO(Connection connection) {
		this.connection = connection;
	}
	
	public List<Feedback> loadListFeedback(Integer feedbackID, Integer bookingID, Integer tutorID) {
	    List<Feedback> fbList = new ArrayList<>();
	    StringBuilder sql = new StringBuilder("SELECT * FROM feedback WHERE 1=1");
	    List<Object> parameters = new ArrayList<>();

	    // Dynamically build the query and add parameters
	    if (feedbackID != null) {
	        sql.append(" AND feedback_id = ?");
	        parameters.add(feedbackID);
	    }
	    if (bookingID != null) {
	        sql.append(" AND booking_id = ?");
	        parameters.add(bookingID);
	    }
	    if (tutorID != null) {
	        sql.append(" AND tutor_id = ?");
	        parameters.add(tutorID);
	    }

	    try (PreparedStatement statement = connection.prepareStatement(sql.toString())) {
	        // Bind the parameters dynamically
	        for (int i = 0; i < parameters.size(); i++) {
	            Object param = parameters.get(i);
	            if (param instanceof Integer) {
	                statement.setInt(i + 1, (Integer) param);
	            } else if (param instanceof String) {
	                statement.setString(i + 1, (String) param);
	            }
	        }

	        ResultSet rs = statement.executeQuery();
	        while (rs.next()) {
	            Feedback fb = loadFeedbackFromRs(rs);
	            fbList.add(fb);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println("Could not load from SQL: FEEDBACK DATA");
	    }

	    return fbList;
	}
	
	public Feedback loadSingleFeedback(Integer feedbackID, Integer bookingID, Integer tutorID) {
		Feedback feedback = null;
		StringBuilder sql = new StringBuilder("SELECT * FROM feedback WHERE 1=1");
		List<Object> parameters = new ArrayList<>();

		// Dynamically build the query and add parameters
		if (feedbackID != null) {
			sql.append(" AND feedback_id = ?");
			parameters.add(feedbackID);
		}
		if (bookingID != null) {
			sql.append(" AND booking_id = ?");
			parameters.add(bookingID);
		}
		if (tutorID != null) {
			sql.append(" AND tutor_id = ?");
			parameters.add(tutorID);
		}

		try (PreparedStatement statement = connection.prepareStatement(sql.toString())) {
			// Bind the parameters dynamically
			for (int i = 0; i < parameters.size(); i++) {
				Object param = parameters.get(i);
				if (param instanceof Integer) {
					statement.setInt(i + 1, (Integer) param);
				} else if (param instanceof String) {
					statement.setString(i + 1, (String) param);
				}
			}

			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				feedback = loadFeedbackFromRs(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Could not load from SQL: FEEDBACK DATA");
		}

		return feedback;
	}

	/**
	 * Creates a Feedback object from the given ResultSet.
	 * 
	 * @param rs the ResultSet containing the feedback data
	 * @return the Feedback object
	 * @throws SQLException if any database access error occurs
	 */
	private Feedback loadFeedbackFromRs(ResultSet rs) throws SQLException {
	    // Create UserDAO for retrieving tutor name
	    UserDAO userDAO = new UserDAO(connection);
	    String tutorName = userDAO.getTutorNameByID(rs.getInt("tutor_id"));

	    return new Feedback(
	        rs.getInt("feedback_id"),
	        rs.getInt("booking_id"),
	        rs.getInt("tutor_id"),
	        tutorName,
	        rs.getInt("rating"),
	        rs.getString("comment"),
	        rs.getTimestamp("created_at")
	    );
	}

	
	public Feedback addFeedback(Feedback feedback) {
        String sql = "INSERT INTO feedback (booking_id, rating, comment, tutor_id) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
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
    
	public double getAverageRating(int tutorID) {
		String sql = "SELECT AVG(rating) FROM feedback WHERE tutor_id = ?";

		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, tutorID);

			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				return rs.getDouble(1);
			}
		} catch (SQLException e) {
			System.err.println("Failed to get feedback average: " + e.getMessage());
		}
		return 0;
	}
}
