package com.algonquin.ToiTalk.controller;


import com.algonquin.ToiTalk.DAO.LanguageDAO;
import com.algonquin.ToiTalk.DAO.UserDAO;
import com.algonquin.ToiTalk.model.User;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@WebServlet("/loadRegisterServlet")
public class LoadRegisterServlet extends HttpServlet {
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
        try (Connection connection = dataSource.getConnection()) {
        LanguageDAO languageDAO = new LanguageDAO(connection);
        	
        // Forward to profile.jsp to display profile information
        request.setAttribute("allLang", languageDAO.listStrAllLang());
        request.getRequestDispatcher("/register.jsp").forward(request, response);
		} catch (SQLException e) {
			response.sendRedirect("signin.jsp");
			throw new ServletException("Cannot obtain a connection", e);
		}
    }
}