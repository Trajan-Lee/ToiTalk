package com.algonquin.ToiTalk.controller;

import com.algonquin.ToiTalk.model.*;
import com.algonquin.ToiTalk.DAO.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {
	private UserDAO userDAO;
	private Connection connection;
	
	
	@Override
	public void init() throws ServletException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/toitalk", "user1", "password1");
			userDAO = new UserDAO(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
            throw new ServletException("MySQL JDBC Driver not found", e);
        }
	}
	
	@Override
	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		
		User user = userDAO.validateUser(email, password);
		if (user != null) {
			request.getSession().setAttribute("user", user);
			//response.sendRedirect("dashboard.jsp");
			System.out.println("user validated");
		} else {
			request.setAttribute("errorMessage","Invalid username or password");
		}
	}
}
