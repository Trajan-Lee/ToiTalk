package com.algonquin.ToiTalk.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import java.time.LocalDateTime;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;

import com.algonquin.ToiTalk.DAO.BookingDAO;
import com.algonquin.ToiTalk.DAO.UserDAO;
import com.algonquin.ToiTalk.model.Booking;
import com.algonquin.ToiTalk.model.Student;
import com.algonquin.ToiTalk.model.Tutor;

@WebServlet("/confirmBookingServlet")
public class ConfirmBookingServlet extends HttpServlet {

    private DataSource dataSource;

    @Override
    public void init() throws ServletException {
        try {
            // Initialize DataSource from JNDI
            InitialContext ctx = new InitialContext();
            dataSource = (DataSource) ctx.lookup("java:comp/env/jdbc/toitalk");
        } catch (NamingException e) {
            throw new ServletException("Cannot retrieve JNDI DataSource", e);
        }
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		try (Connection connection = dataSource.getConnection()) {
			UserDAO userDAO = new UserDAO(connection);
			BookingDAO bookingDAO = new BookingDAO(connection);
			connection.setAutoCommit(false);
	        // Get form parameters
	        int tutorId = Integer.parseInt(request.getParameter("tutorId"));
	        int day = Integer.parseInt(request.getParameter("day"));
	        System.out.println("day: " + day);
	        int hour = Integer.parseInt(request.getParameter("hour"));
	        System.out.println("hour: " + hour);
	        int weeks = Integer.parseInt(request.getParameter("weeks"));
	
	        Student student = (Student) request.getSession().getAttribute("user");
	        Tutor tutor = userDAO.getTutorObjByID(tutorId);
	
	        String tutorName = tutor.getUsername();
	        String studentName = student.getUsername();
	
	        List<Booking> bookings = new ArrayList<>();
	
	        boolean success = true;
	        boolean overlapError = false;
	        int[] failedWeeks = new int[weeks];
	
	        // Add bookings to a booking list
	        for (int week = 0; week < weeks; week++) {
	            Timestamp bookingTimestamp = generateBookingTimestamp(week, day, hour);
	            overlapError = bookingDAO.checkBookingTimeOverlap(bookingTimestamp);
	            if (overlapError) {
	                failedWeeks[week] = week + 1;
	                success = false;
	            } else {
	                Booking booking = new Booking(0, tutorId, student.getStudentID(), tutorName, studentName, bookingTimestamp, "Scheduled", false);
	                bookings.add(booking);
	            }
	        }
	
	        // If no overlap, add bookings to the database
	        if (success) {
	            for (Booking booking : bookings) {
	                booking = bookingDAO.addBooking(booking);
	                success = success && (booking.getBookingID() != 0);
	                if (!success) {
	                    break;
	                }
	            }
	        }
	
	        // Prepare response based on success or failure
	        if (success) {
	            connection.commit();
	            // Redirect to the booking page with the confirmation message
	            response.sendRedirect("viewBookingServlet");
	        } else {
	            connection.rollback();
	            if (overlapError) {
	            	int count = 0;
	                StringBuilder message = new StringBuilder("Sorry, there are overlaps in the following weeks: ");
	                for (int week : failedWeeks) {
	                	if (count == 0) {
	                    message.append(week);
						} else {
							message.append(", " + week);
						}
	                }
	                //use <br> to break line in jsp
	                message.append("<br>Please avoid these timeslots by changing the time or the number of weeks.");
	                request.setAttribute("message", message.toString());
	            } else {
	                request.setAttribute("message", "Sorry, there was an issue confirming your booking. Please try again later.");
	            }
	        }
	        
	        try {
	            connection.setAutoCommit(true);
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
        
    	} catch (SQLException e) {
    		System.out.println("SQLException: " + e.getMessage());
			e.printStackTrace();
		}
    }

    // Generate a booking timestamp based on the week, day, and hour, always setting the minute and second to 30
	public static Timestamp generateBookingTimestamp(int week, int day, int hour) {
	    LocalDateTime localDateTime = LocalDateTime.now();
	    LocalDateTime bookingDateTime = localDateTime.plusWeeks(week)
	                                                 .with(DayOfWeek.of(day + 1))
	                                                 .withHour(hour)
	                                                 .withMinute(30)
	                                                 .withSecond(0)
	                                                 .withNano(0);
	    return Timestamp.valueOf(bookingDateTime);
	}
}