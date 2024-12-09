
package com.algonquin.ToiTalk.controller;

import java.io.IOException;
import java.sql.Connection;
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

import com.algonquin.ToiTalk.DAO.FeedbackDAO;
import com.algonquin.ToiTalk.DAO.UserDAO;
import com.algonquin.ToiTalk.model.Feedback;
import com.algonquin.ToiTalk.model.User;

@WebServlet("/saveFeedbackServlet")
public class SaveFeedbackServlet extends HttpServlet {

    private DataSource dataSource;

    @Override
    public void init() throws ServletException {
        try {
            InitialContext ctx = new InitialContext();
            dataSource = (DataSource) ctx.lookup("java:comp/env/jdbc/toitalk");
        } catch (NamingException e) {
            throw new ServletException("Cannot retrieve JNDI DataSource", e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (Connection connection = dataSource.getConnection()) {
        	StringBuilder errorMessage = new StringBuilder();
        	connection.setAutoCommit(false);
            FeedbackDAO feedbackDAO = new FeedbackDAO(connection);
            UserDAO userDAO = new UserDAO(connection);
            Feedback feedbackOld = null;
            Feedback feedbackNew = null;
            boolean success = true;

            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            if (user == null) {
                response.sendRedirect("signin.jsp");
                return;
            }

            int bookingID = Integer.parseInt(request.getParameter("bookingId"));
            int tutorID = Integer.parseInt(request.getParameter("tutorId"));
            int rating = Integer.parseInt(request.getParameter("rating"));
            String comment = request.getParameter("comment");
            boolean feedbackExists = Boolean.parseBoolean(request.getParameter("feedbackExists"));
            if (feedbackExists) {
            	feedbackOld = feedbackDAO.loadSingleFeedback(null, bookingID, null);
            	feedbackNew = feedbackOld;
            	feedbackNew.setRating(rating);
            	feedbackNew.setComment(comment);
            	success = feedbackDAO.updateFeedback(feedbackNew) && success;
			} else {
				String tutorName = userDAO.getTutorNameByID(tutorID);
				feedbackNew = new Feedback(0, bookingID, tutorID, tutorName, rating, comment, null);
				feedbackNew = feedbackDAO.addFeedback(feedbackNew);
				if (feedbackNew.getFeedbackID() == 0) {
					success = false;
				}
			}
			if (success) {
				//commit required to get new average
				connection.commit();
				double averageRating = feedbackDAO.getAverageRating(tutorID);
				boolean ratingSuccess = userDAO.setTutorRating(tutorID, averageRating);
				if (!ratingSuccess) {
                    errorMessage.append("Failed to update tutor rating. Rolling back changes");
                    // rollback manually
					if (feedbackOld != null) {
                        success = feedbackDAO.updateFeedback(feedbackOld);
                    } else {
                        success = feedbackDAO.deleteFeedback(feedbackNew.getFeedbackID());
					} 
					request.setAttribute("errorMessage", errorMessage.toString());
					request.getRequestDispatcher("viewBookingServlet").forward(request, response);
				} else {
					response.sendRedirect("viewBookingServlet");
				}
			} else {
				connection.rollback();
				request.setAttribute("errorMessage", errorMessage.toString());
				request.getRequestDispatcher("viewBookingServlet").forward(request, response);
			}
                
        	try {
        		connection.setAutoCommit(true);
        	} catch (SQLException e) {
        		e.printStackTrace();
        	}
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}
