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
		String sql = "SELECT * FROM bookings "
				+ "WHERE booking_id LIKE ? "
				+ "AND student_id LIKE ? "
				+ "AND tutor_id LIKE ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)){
			//add wildcare to ID if null
			if (bookingID != null) {
				statement.setInt(1, bookingID);
			} else {
				statement.setString(1, "%");
			}
			
			if (studentID != null) {
				statement.setInt(2, studentID);
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
			String studentName;
			while (rs.next()) {
				tutorName = userDAO.getTutorNameByID(rs.getInt("tutor_id"));
				studentName = userDAO.getStudentNameByID(rs.getInt("student_id"));
				Booking book = new Booking(rs.getInt("booking_id"), rs.getInt("tutor_id"), rs.getInt("student_id")
						, rs.getInt("slot_id"), tutorName, studentName, rs.getTimestamp("date"), rs.getString("status"));
				bookList.add(book);
			}
		} catch (SQLException e) {
	    	e.printStackTrace();
			System.out.println("Could not load from SQL: BOOKING DATA");
		}
		
	    return bookList;
	}
	
	
	
    public Booking addBooking(Booking booking) {
        String sql = "INSERT INTO bookings (tutor_id, student_id, slot_id, date, status) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, booking.getTutorID());
            statement.setInt(2, booking.getStudentID());
            statement.setInt(3, booking.getSlotID());
            statement.setTimestamp(4, booking.getDate());
            statement.setString(5, booking.getStatus());

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
        String sql = "UPDATE bookings SET tutor_id = ?, student_id = ?, tutor_schedule_id = ?, date = ?, status = ? WHERE booking_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, booking.getTutorID());
            statement.setInt(2, booking.getStudentID());
            statement.setInt(3, booking.getSlotID());
            statement.setTimestamp(4, booking.getDate());
            statement.setString(5, booking.getStatus());
            statement.setInt(6, booking.getBookingID());

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
				+ "SET status = ?"
				+ "WHERE booking_id = ?";
		
	    try (PreparedStatement statement = connection.prepareStatement(sql)) {
	    	statement.setString(1, status);
	    	statement.setInt(2, bookingID);
	    	return true;
	    	
	    } catch (SQLException e) {
	    	e.printStackTrace();
	    	return false;
	    }
	}
}
