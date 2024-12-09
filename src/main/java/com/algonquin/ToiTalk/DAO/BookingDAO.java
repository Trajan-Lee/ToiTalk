package com.algonquin.ToiTalk.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.algonquin.ToiTalk.model.Booking;

public class BookingDAO {
	Connection connection;
	
	public BookingDAO(Connection connection) {
		this.connection = connection;
	}
	
	
	// more elegent way to dynamically build the query and collect parameters
	public List<Booking> loadListBookings(Integer bookingID, Integer studentID, Integer tutorID, String status) {
	    List<Booking> bookList = new ArrayList<>();

	    StringBuilder sql = new StringBuilder("SELECT * FROM bookings WHERE 1=1");
	    List<Object> parameters = new ArrayList<>();

	    // Dynamically build the query and collect parameters
	    if (bookingID != null) {
	        sql.append(" AND booking_id = ?");
	        parameters.add(bookingID);
	    }
	    if (studentID != null) {
	        sql.append(" AND student_id = ?");
	        parameters.add(studentID);
	    }
	    if (tutorID != null) {
	        sql.append(" AND tutor_id = ?");
	        parameters.add(tutorID);
	    }
	    if (status != null) {
	        sql.append(" AND status = ?");
	        parameters.add(status);
	    }

	    try (PreparedStatement statement = connection.prepareStatement(sql.toString())) {
	        // Bind the parameters in order
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
	            bookList.add(loadBookingFromRS(rs));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println("Could not load from SQL: BOOKING DATA");
	    }

	    return bookList;
	}
	
	public Booking loadBookingFromRS(ResultSet rs) {
		Booking booking = null;
		try {
			UserDAO userDAO = new UserDAO(connection);
			String tutorName = userDAO.getTutorNameByID(rs.getInt("tutor_id"));
			String studentName = userDAO.getStudentNameByID(rs.getInt("student_id"));
			booking = new Booking(rs.getInt("booking_id"), rs.getInt("tutor_id"), rs.getInt("student_id"), tutorName,
					studentName, rs.getTimestamp("date"), rs.getString("status"), false);
			booking.setFeedback(checkFeedbackAdded(rs.getInt("booking_id")));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return booking;
	}
	
	public Boolean checkBookingTimeOverlap(Timestamp date) {
	    String sql = "SELECT COUNT(*) FROM bookings WHERE date >= ? AND date < ?";
	    Timestamp start = Timestamp.valueOf(date.toLocalDateTime().withMinute(0).withSecond(0).withNano(0));
	    Timestamp end = Timestamp.valueOf(date.toLocalDateTime().plusHours(1).withMinute(0).withSecond(0).withNano(0));
	    try (PreparedStatement statement = connection.prepareStatement(sql)) {
	        statement.setTimestamp(1, start);
	        statement.setTimestamp(2, end);
	        ResultSet rs = statement.executeQuery();
	        if (rs.next()) {
	            return rs.getInt(1) > 0;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	
    public Booking addBooking(Booking booking) {
        String sql = "INSERT INTO bookings (tutor_id, student_id, date, status) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, booking.getTutorID());
            statement.setInt(2, booking.getStudentID());
            statement.setTimestamp(3, booking.getDate());
            statement.setString(4, booking.getStatus());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                // Retrieve the generated keys
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        // Get the auto-incremented feedback_id
                        booking.setBookingID(generatedKeys.getInt(1));
                    } else {
                        System.err.println("No feedback ID obtained.");
                    }
                }
            }
            return booking;
        } catch (SQLException e) {
            System.err.println("Failed to add booking: " + e.getMessage());
            return booking;
        }
    }

    public boolean updateBooking(Booking booking) {
        String sql = "UPDATE bookings SET tutor_id = ?, student_id = ?, date = ?, status = ? WHERE booking_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, booking.getTutorID());
            statement.setInt(2, booking.getStudentID());
            statement.setTimestamp(3, booking.getDate());
            statement.setString(4, booking.getStatus());
            statement.setInt(5, booking.getBookingID());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Failed to update booking: " + e.getMessage());
            return false;
        }
    }
	
    public boolean deleteBooking(int bookingId) {
        String sql = "DELETE FROM bookings WHERE booking_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, bookingId);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Failed to delete booking: " + e.getMessage());
            return false;
        }
    }
	
    public boolean changeBookingStatus(int bookingID, String status) {
        String sql = "UPDATE bookings"
            + " SET status = ?"
            + " WHERE booking_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status);
            statement.setInt(2, bookingID);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
	
	public boolean checkFeedbackAdded(int bookingID) {
	    // SQL query to check if feedback exists for the given bookingID
	    String sql = "SELECT 1 "
	               + "FROM bookings "
	               + "INNER JOIN feedback "
	               + "ON bookings.booking_id = feedback.booking_id "
	               + "WHERE bookings.booking_id = ?";

	    try (PreparedStatement statement = connection.prepareStatement(sql)) {
	        // Set the bookingID parameter
	        statement.setInt(1, bookingID);
	        
	        // Execute the query
	        ResultSet rs = statement.executeQuery();
	        
	        // Check if a record exists
	        return rs.next();
	    } catch (SQLException e) {
	        // Log the error and return false
	        System.err.println("Error checking feedback status for bookingID: " + bookingID);
	        e.printStackTrace();
	        return false;
	    }
	}

}
