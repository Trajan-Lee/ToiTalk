package com.algonquin.ToiTalk.controller;


import com.algonquin.ToiTalk.DAO.LanguageDAO;
import com.algonquin.ToiTalk.DAO.UserDAO;
import com.algonquin.ToiTalk.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@WebServlet("/loadEditProfileServlet")
public class LoadEditProfileServlet extends HttpServlet {
	Connection connection;
	LanguageDAO languageDAO;
	
	@Override
	public void init() throws ServletException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/toitalk", "user1", "password1");
			languageDAO = new LanguageDAO(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
            throw new ServletException("MySQL JDBC Driver not found", e);
        }
	}
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            response.sendRedirect("signin.jsp"); // Redirect if not logged in
            return;
        }
        
        // Forward to profile.jsp to display profile information
        request.setAttribute("user", user);
        request.setAttribute("allLang", languageDAO.listStrAllLang());
        request.getRequestDispatcher("/editProfile.jsp").forward(request, response);
    }
}
