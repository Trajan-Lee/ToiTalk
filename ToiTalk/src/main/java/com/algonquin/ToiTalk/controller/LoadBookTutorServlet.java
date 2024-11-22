package com.algonquin.ToiTalk.controller;

import com.algonquin.ToiTalk.DAO.UserDAO;
import com.algonquin.ToiTalk.model.Tutor;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@WebServlet("/loadBookTutorServlet")
public class LoadBookTutorServlet extends HttpServlet {
    private UserDAO userDAO;
    Connection connection;

    @Override
	public void init() throws ServletException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/toitalk", "user1", "password1");
			this.userDAO = new UserDAO(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
	        throw new ServletException("MySQL JDBC Driver not found", e);
		}
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the tutorId from the request parameter
        String tutorIDParam = request.getParameter("tutorId");
        String schedule[][];

        // Check if tutorId is present and valid
        if (tutorIDParam != null) {
            try {
                int tutorID = Integer.parseInt(tutorIDParam); // Convert to int
                Tutor tutor = userDAO.getTutorObjByID(tutorID); // Retrieve Tutor object by ID

                if (tutor != null) {
                	//Set attribute for schedule
                	schedule = tutor.getSchedule().getArrSchedule();
                    // Set the Tutor object as a request attribute to pass it to the JSP
                    request.setAttribute("schedule", schedule);
                    request.setAttribute("tutorId", tutorID);
                    // Forward to the tutor profile JSP page
                    request.getRequestDispatcher("/bookTutor.jsp").forward(request, response);
                    return;
                } else {
                    // Tutor not found, set an error message
                    request.setAttribute("errorMessage", "Tutor not found.");
                }
            } catch (NumberFormatException e) {
                // Invalid tutorId format, set an error message
                request.setAttribute("errorMessage", "Invalid tutor ID format.");
            }
        } else {
            // tutorId parameter is missing
            request.setAttribute("errorMessage", "Missing tutor ID.");
        }

        // If an error occurs, forward to an error page or display an error message
        request.getRequestDispatcher("/error.jsp").forward(request, response);
    }

}
