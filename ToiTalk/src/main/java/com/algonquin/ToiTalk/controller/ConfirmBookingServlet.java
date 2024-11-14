package com.algonquin.ToiTalk.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.sql.Timestamp;

import com.algonquin.ToiTalk.DAO.BookingDAO;
import com.algonquin.ToiTalk.DAO.ScheduleDAO;
import com.algonquin.ToiTalk.DAO.UserDAO;
import com.algonquin.ToiTalk.model.Booking;
import com.algonquin.ToiTalk.model.Schedule;
import com.algonquin.ToiTalk.model.Student;
import com.algonquin.ToiTalk.model.Tutor;

@WebServlet("/confirmBookingServlet")
public class ConfirmBookingServlet extends HttpServlet {
	UserDAO userDAO;
    BookingDAO bookingDAO;
    ScheduleDAO scheduleDAO;
    Connection connection;

    @Override
	public void init() throws ServletException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/toitalk", "user1", "password1");
			this.userDAO = new UserDAO(connection);
	        this.bookingDAO = new BookingDAO(connection);
	        this.scheduleDAO = new ScheduleDAO(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
	        throw new ServletException("MySQL JDBC Driver not found", e);
		}
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get form parameters
        int tutorId = Integer.parseInt(request.getParameter("tutorId"));
        int day = Integer.parseInt(request.getParameter("day"));
        int hour = Integer.parseInt(request.getParameter("hour"));
        int slotID = scheduleDAO.generateSlotID(day, hour);
        
        Student student = (Student) request.getSession().getAttribute("user");
        Tutor tutor = userDAO.getTutorObjByID(tutorId);
        
        String tutorName = tutor.getUsername();
        String studentName = student.getUsername();
        
        LocalDateTime localDateTime = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(localDateTime);
        
        String[][] ArrSchedule = tutor.getSchedule().getArrSchedule();
        
        //update tutor's schedule to mark as booked now
        if (ArrSchedule[day][hour].equals("Open")){
        	ArrSchedule[day][hour] = "Booked";
        	tutor.getSchedule().setArrSchedule(ArrSchedule);
        }
        
        Booking booking = new Booking(0, tutorId, student.getStudentID(), slotID, tutorName, studentName, timestamp, "Scheduled");
        // Book the session
        booking = bookingDAO.addBooking(booking);
        
        Boolean success = (booking.getBookingID() != 0) && scheduleDAO.addSchedule(tutor.getSchedule());
        
        // Prepare response based on success or failure
        if (success) {
            request.setAttribute("message", "Booking confirmed successfully!");
        } else {
            request.setAttribute("message", "Sorry, there was an issue confirming your booking.");
        }

        // Redirect to the tutor's profile or booking page with the confirmation message
        request.getRequestDispatcher("/bookTutor.jsp").forward(request, response);
    }
}