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

@WebServlet("/changeBookingStatusServlet")
public class ChangeBookingStatusServlet extends HttpServlet {

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
    
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try (Connection connection = dataSource.getConnection()) {
			BookingDAO bookingDAO = new BookingDAO(connection);
			// Get form parameters
			int bookingId = Integer.parseInt(request.getParameter("bookingId"));
			String status = request.getParameter("changeStatus");
			System.out.println("bookingId: " + bookingId);
			System.out.println("status: " + status);
			boolean success = bookingDAO.changeBookingStatus(bookingId, status);
			System.out.println("success: " + success);
			response.sendRedirect("viewBookingServlet");
		} catch (SQLException e) {
			e.printStackTrace();
			response.sendRedirect("viewBookingServlet");
		}
    }
}