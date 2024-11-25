package com.algonquin.ToiTalk.controller;

import com.algonquin.ToiTalk.model.User;
import com.algonquin.ToiTalk.model.Tutor;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/loadProfileServlet")
public class LoadProfileServlet extends HttpServlet {
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
        request.getRequestDispatcher("/userProfile.jsp").forward(request, response);
    }
}
