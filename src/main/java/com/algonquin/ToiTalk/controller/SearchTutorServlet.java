package com.algonquin.ToiTalk.controller;


import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.algonquin.ToiTalk.DAO.LanguageDAO;
import com.algonquin.ToiTalk.DAO.UserDAO;
import com.algonquin.ToiTalk.model.User;
import com.google.gson.Gson;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/searchTutorServlet")
public class SearchTutorServlet extends HttpServlet {
	UserDAO userDAO;
	LanguageDAO langDAO;
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
        try (Connection connection = dataSource.getConnection()) {
        	langDAO = new LanguageDAO(connection);
        	userDAO = new UserDAO(connection);

	        String searchInput = request.getParameter("searchInput");
	        String searchType = request.getParameter("searchType");
	    	//repopulate the language list
	        List<String> arrList = langDAO.listStrAllLang();
		     String json = new Gson().toJson(arrList);
	    	request.setAttribute("allLang", json);
	        boolean langYes = "language".equals(searchType); // true if searching by language
	
	        List<User> tutorResults = userDAO.searchTutorByNameOrLang(searchInput, langYes);
	
	        request.setAttribute("tutorResults", tutorResults);
	        
	        request.getRequestDispatcher("searchTutor.jsp").forward(request, response);
		} catch (SQLException e) {
			throw new ServletException("SQL error when searching for tutors", e);
		}
    }
}
