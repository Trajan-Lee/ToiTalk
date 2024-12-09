
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

@WebServlet("/viewBookingServlet")
public class ViewBookingServlet extends HttpServlet {
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuilder errorMessage = new StringBuilder();

        try (Connection connection = dataSource.getConnection()) {
        	BookingDAO bookingDAO = new BookingDAO(connection);
        	List<Booking> bookings;
            User user = (User) request.getSession().getAttribute("user");
            String status = request.getParameter("status");
            
            if (user == null) {
                response.sendRedirect("signin.jsp");
                return;
            }
            
            if (status != null) {
            	if (status.equals("all")) {
    	    		if (user.getType().equals("tutor")) {
    	    			Tutor tutor = (Tutor) user;
    	    			bookings = bookingDAO.loadListBookings(null, null, tutor.getTutorID(), null);
    	    			request.setAttribute("bookings", bookings);
    	    		} else if (user.getType().equals("student")) {
    	    			Student student = (Student) user;
    	    			bookings = bookingDAO.loadListBookings(null, student.getStudentID(), null, null);
    	    			request.setAttribute("bookings", bookings);
    	    		}
            	} else {
		    		if (user.getType().equals("tutor")) {
		    			Tutor tutor = (Tutor) user;
		    			bookings = bookingDAO.loadListBookings(null, null, tutor.getTutorID(), status);
		    			request.setAttribute("bookings", bookings);
		    		} else if (user.getType().equals("student")) {
		    			Student student = (Student) user;
		    			bookings = bookingDAO.loadListBookings(null, student.getStudentID(), null, status);
		    			request.setAttribute("bookings", bookings);
		    		}
            	}
            }

    		request.setAttribute("status", status);
    		request.getRequestDispatcher("/viewBookings.jsp").forward(request, response);
        } catch (SQLException e) {
            errorMessage.append("General error loading bookings");
            request.setAttribute("errorMessage", errorMessage.toString());
            request.getRequestDispatcher("/viewBookings.jsp").forward(request, response);
            e.printStackTrace();
        }
    }
}
