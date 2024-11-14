package com.algonquin.ToiTalk.controller;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.algonquin.ToiTalk.DAO.LanguageDAO;
import com.algonquin.ToiTalk.DAO.UserDAO;
import com.algonquin.ToiTalk.model.User;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/searchTutorServlet")
public class SearchTutorServlet extends HttpServlet {
	UserDAO userDAO;
	LanguageDAO langDAO;
	private Connection connection;
	
	
	@Override
	public void init() throws ServletException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/toitalk", "user1", "password1");
			this.userDAO = new UserDAO(connection);
			this.langDAO = new LanguageDAO(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
            throw new ServletException("MySQL JDBC Driver not found", e);
        }
	}

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String searchInput = request.getParameter("searchInput");
        String searchType = request.getParameter("searchType");
        List<String> allLangList = langDAO.listAllLang();
        request.setAttribute("langList", allLangList);
        boolean langYes = "language".equals(searchType); // true if searching by language

        List<User> tutorResults = userDAO.searchTutorByNameOrLang(searchInput, langYes);

        request.setAttribute("tutorResults", tutorResults);
        
        request.getRequestDispatcher("searchTutor.jsp").forward(request, response);
    }
}
