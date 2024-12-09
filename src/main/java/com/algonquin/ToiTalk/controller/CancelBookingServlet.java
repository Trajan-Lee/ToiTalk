
package com.algonquin.ToiTalk.controller;

import com.algonquin.ToiTalk.DAO.BookingDAO;
import com.algonquin.ToiTalk.model.Booking;
import com.algonquin.ToiTalk.model.Student;
import com.algonquin.ToiTalk.model.Tutor;
import com.algonquin.ToiTalk.model.User;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/cancelBookingServlet")
public class CancelBookingServlet extends HttpServlet {
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuilder errorMessage = new StringBuilder();
        boolean success = true;

        try (Connection connection = dataSource.getConnection()) {
        	connection.setAutoCommit(false);
        	BookingDAO bookingDAO = new BookingDAO(connection);
        	
        	List<Booking> bookings = (List<Booking>) request.getAttribute("bookings");
            User user = (User) request.getSession().getAttribute("user");
            String status = (String) request.getAttribute("status");
            int bookingID = Integer.parseInt(request.getParameter("bookingID"));
            
            if (user == null) {
                response.sendRedirect("signin.jsp");
                return;
            }
            
            int initialSize = bookings.size();
            bookings = deleteListBooking(bookings, bookingID);
            if (bookings.size() == initialSize) {
            	success = false;
            	errorMessage.append("Error deleting booking from List. ");
            }
			if (!bookingDAO.changeBookingStatus(bookingID, "Cancelled")) {
				success = false;
				errorMessage.append("Error updating booking status in database. ");
			}
            
			if (success) {
				connection.commit();
			} else {
				connection.rollback();
	            request.setAttribute("errorMessage", errorMessage.toString());
			}
			request.setAttribute("bookings", bookings);
            request.getRequestDispatcher("/viewBookings.jsp").forward(request, response);
        	try {
        		connection.setAutoCommit(true);
        	} catch (SQLException e) {
        		e.printStackTrace();
        	}
        } catch (SQLException e) {
            errorMessage.append("General error deleting booking. ");
            request.setAttribute("errorMessage", errorMessage.toString());
            request.getRequestDispatcher("/viewBookings.jsp").forward(request, response);
            e.printStackTrace();
        }
    }
    
	private List<Booking> deleteListBooking(List<Booking> bookings, int bookingID) {
        bookings.removeIf(booking -> booking.getBookingID() == bookingID);
		return bookings;
	}
}
