package com.algonquin.ToiTalk.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.algonquin.ToiTalk.DAO.LanguageDAO;
import com.algonquin.ToiTalk.DAO.UserDAO;
import com.algonquin.ToiTalk.model.Language;
import com.algonquin.ToiTalk.model.Tutor;
import com.algonquin.ToiTalk.model.User;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/updateProfile")
public class UpdateProfileServlet extends HttpServlet {
	Connection connection;
	LanguageDAO languageDAO;
	UserDAO userDAO;
	
	@Override
	public void init() throws ServletException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/toitalk", "user1", "password1");
			languageDAO = new LanguageDAO(connection);
			userDAO = new UserDAO(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
            throw new ServletException("MySQL JDBC Driver not found", e);
        }
	}
	
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Update common fields
        user.setUsername(request.getParameter("username"));
        user.setEmail(request.getParameter("email"));


        // Update tutor-specific fields if the user is a tutor
        if ("Tutor".equals(user.getType()) && user instanceof Tutor) {
            Tutor tutor = (Tutor) user;
            tutor.setBio(request.getParameter("bio"));
            tutor.setExpYears(Integer.parseInt(request.getParameter("expYears")));

            String[] selectedLanguages = request.getParameterValues("languages");
            List<Language> languages = new ArrayList<>();
            for (String lang : selectedLanguages) {
                languages.add(languageDAO.getLangByName(lang)); // assuming a Language constructor that takes a language name
            }
            tutor.setLanguages(languages);
            userDAO.updateTutor(tutor);
        }

        // Update session attribute with the modified user
        session.setAttribute("user", user);

        // Redirect back to the profile page after saving changes
        response.sendRedirect("profile.jsp");
    }
}
