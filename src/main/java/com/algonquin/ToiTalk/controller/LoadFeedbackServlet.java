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
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import java.time.LocalDateTime;
import java.time.DayOfWeek;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;

import com.algonquin.ToiTalk.DAO.BookingDAO;
import com.algonquin.ToiTalk.DAO.FeedbackDAO;
import com.algonquin.ToiTalk.DAO.ScheduleDAO;
import com.algonquin.ToiTalk.DAO.UserDAO;
import com.algonquin.ToiTalk.model.Booking;
import com.algonquin.ToiTalk.model.Feedback;
import com.algonquin.ToiTalk.model.Schedule;
import com.algonquin.ToiTalk.model.Student;
import com.algonquin.ToiTalk.model.Tutor;
import com.algonquin.ToiTalk.model.User;

@WebServlet("/loadFeedbackServlet")
public class LoadFeedbackServlet extends HttpServlet {

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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try (Connection connection = dataSource.getConnection()) {
			FeedbackDAO feedbackDAO = new FeedbackDAO(connection);
			
			connection.setAutoCommit(false);
			
	        HttpSession session = request.getSession();
	        User user = (User) session.getAttribute("user");
			if (user == null) {
				response.sendRedirect("signin.jsp");
				return;
			}
	        
	        int bookingID = Integer.parseInt(request.getParameter("bookingId"));
	        int tutorID = Integer.parseInt(request.getParameter("tutorId"));
	        boolean feedbackExists = Boolean.parseBoolean(request.getParameter("feedbackExists"));
	        
	        //creates a reverse list from 5 to 1. Is a work around due to step not being able to count down
	        List<Integer> reverseList = new ArrayList<>();
	        for (int i = 5; i >= 1; i--) {
	            reverseList.add(i);
	        }
	        request.setAttribute("starList", reverseList);

			if (feedbackExists) {
				Feedback feedback = feedbackDAO.loadSingleFeedback(null, bookingID, null);
				if (feedback != null) {
					request.setAttribute("feedback", feedback);
					request.setAttribute("bookingId", bookingID);
					request.setAttribute("tutorId", tutorID);
					request.setAttribute("feedbackExists", feedbackExists);
					request.getRequestDispatcher("changeFeedback.jsp").forward(request, response);
				} else {
					request.setAttribute("errorMessage", "error loading saved feedback");
	                request.getRequestDispatcher("viewBookings.jsp").forward(request, response);
				}
			} else {
				request.setAttribute("bookingId", bookingID);
				request.setAttribute("tutorId", tutorID);
				request.setAttribute("feedbackExists", feedbackExists);
                request.getRequestDispatcher("changeFeedback.jsp").forward(request, response);
			}
			
		} catch (SQLException e) {
    		System.out.println("SQLException: " + e.getMessage());
			e.printStackTrace();
		}
    }
}