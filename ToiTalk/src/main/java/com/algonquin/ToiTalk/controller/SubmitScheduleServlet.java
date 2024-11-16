package com.algonquin.ToiTalk.controller;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.algonquin.ToiTalk.DAO.LanguageDAO;
import com.algonquin.ToiTalk.DAO.ScheduleDAO;
import com.algonquin.ToiTalk.DAO.UserDAO;
import com.algonquin.ToiTalk.model.Tutor;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@WebServlet("/submitScheduleServlet")
public class SubmitScheduleServlet extends HttpServlet {
	Connection connection;
	ScheduleDAO scheduleDAO;
	
    @Override
	public void init() throws ServletException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/toitalk", "user1", "password1");
			this.scheduleDAO = new ScheduleDAO(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
            throw new ServletException("MySQL JDBC Driver not found", e);
        }
	}
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Tutor tutor = (Tutor) session.getAttribute("user"); // Tutor object stored in the session


    	
        if (tutor == null) {
            // Redirect to login if tutor is not logged in
            response.sendRedirect("signin.jsp");
            return;
        }

        // Initialize a 2D array to hold the updated schedule
        String[][] updatedSchedule = new String[7][24]; // 7 days, 24 hours

        // Loop through the days and hours to get the updated schedule from request parameters
        for (int day = 0; day < 7; day++) {
            for (int hour = 0; hour < 24; hour++) {
                String paramName = "schedule-" + day + "-" + hour;
                String scheduleValue = request.getParameter(paramName);
                if (scheduleValue != "") {
                	updatedSchedule[day][hour] = scheduleValue;
                	System.out.println(scheduleValue);
                } else {
                	updatedSchedule[day][hour] = null;
                }
            }
        }
        // Set the updated schedule for the tutor
        tutor.getSchedule().setArrSchedule(updatedSchedule);
        if (scheduleDAO.addSchedule(tutor.getSchedule())) {
            //set session attribute to updated object if DB update successfule
            session.setAttribute("user", tutor);
        }
        
        //  go back to profile
        response.sendRedirect("loadProfileServlet");

    }
}