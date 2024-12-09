
package com.algonquin.ToiTalk.controller;

import com.algonquin.ToiTalk.DAO.LanguageDAO;
import com.algonquin.ToiTalk.DAO.UserDAO;
import com.algonquin.ToiTalk.model.Language;
import com.algonquin.ToiTalk.model.Student;
import com.algonquin.ToiTalk.model.Tutor;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebServlet("/createUserServlet")
public class CreateUserServlet extends HttpServlet {
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean success = true;
        StringBuilder errorMessage = new StringBuilder();

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);

            // Initialize DAOs with the connection
            UserDAO userDAO = new UserDAO(connection);
            LanguageDAO languageDAO = new LanguageDAO(connection);

            // Retrieve form parameters
            String username = request.getParameter("username");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            boolean isTutor = "tutor".equals(request.getParameter("userType"));

            // Set the createTimestamp to now
            Timestamp timestamp = new Timestamp(new Date().getTime());

            // Check if username or email is already taken
            if (userDAO.isUsernameTaken(username)) {
                errorMessage.append("Username is already taken.<br>");
                success = false;
            }
            if (userDAO.isEmailTaken(email)) {
                errorMessage.append("Email is already taken.<br>");
                success = false;
            }

            // If username and email not taken, proceed with user creation
            if (success) {
                if (isTutor) {
                    // Retrieve tutor-specific parameters
                    String bio = request.getParameter("bio");
                    int expYears = Integer.parseInt(request.getParameter("expYears"));
                    String[] languages = request.getParameterValues("languages");

                    // Create List<Language> from languages[]
                    List<Language> languageList = new ArrayList<>();
                    if (languages != null) {
                        for (String lang : languages) {
                            languageList.add(languageDAO.getLangByName(lang));
                        }
                    } else {
                        errorMessage.append("Please select at least one language.<br>");
                        success = false;
                    }

                    // Create tutor if no errors
                    if (success) {
                        Tutor tutor = new Tutor(username, password, email, "tutor", 0, timestamp, 0, languageList, bio, 5, expYears, null);
                        tutor = userDAO.createTutor(tutor);
                        if (tutor.getTutorID() == 0 || tutor.getUserID() == 0) {
                            errorMessage.append("Failed to create tutor, try again later.<br>");
                            success = false;
                        }
                    }
                } else {
                    // Create student
                    Student student = new Student(username, password, email, "student", 0, timestamp, 0);
                    student = userDAO.createStudent(student);
                    if (student.getStudentID() == 0 || student.getUserID() == 0) {
                        errorMessage.append("Failed to create student, try again later.<br>");
                        success = false;
                    }
                }
            }

            if (success) {
                connection.commit();
                response.sendRedirect("signin.jsp");
            } else {
                connection.rollback();
                request.setAttribute("errorMessage", errorMessage.toString());
                request.getRequestDispatcher("register.jsp").forward(request, response);
            }
            
            
        } catch (SQLException e) {
            errorMessage.append("General error creating user, try again later.");
            request.setAttribute("errorMessage", errorMessage.toString());
            request.getRequestDispatcher("register.jsp").forward(request, response);
            e.printStackTrace();
        }
    }
}
