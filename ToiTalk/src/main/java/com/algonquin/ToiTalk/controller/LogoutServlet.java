package com.algonquin.ToiTalk.controller;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/logoutServlet")
public class LogoutServlet extends HttpServlet implements Servlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Invalidate the session to log the user out
        HttpSession session = request.getSession(false); // false to not create a new session if it doesn't exist
        if (session != null) {
            session.invalidate(); // Invalidate the session, effectively logging out the user
        }
        
        // Redirect to the signin page after logout
        response.sendRedirect("signin.jsp");
    }

}
