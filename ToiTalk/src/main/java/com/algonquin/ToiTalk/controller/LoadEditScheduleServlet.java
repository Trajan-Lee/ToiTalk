package com.algonquin.ToiTalk.controller;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.algonquin.ToiTalk.model.Tutor;
import com.algonquin.ToiTalk.model.User;

import java.io.*;
import java.sql.*;

@WebServlet("/loadEditScheduleServlet")
public class LoadEditScheduleServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Assuming we have a tutor ID from the session or request
        HttpSession session = request.getSession();
        Tutor tutor = (Tutor) session.getAttribute("user"); // Tutor object stored in the session

        if (tutor == null) {
            // Redirect to login if tutor is not logged in
            response.sendRedirect("signin.jsp");
            return;
        }

        // Retrieve tutor's current schedule (String[7][24])
        String[][] schedule = tutor.getSchedule().getArrSchedule();

        // Set the schedule as a request attribute
        request.setAttribute("tutorSchedule", schedule);

        // Forward the request to the JSP page to display the schedule
        RequestDispatcher dispatcher = request.getRequestDispatcher("editSchedule.jsp");
        dispatcher.forward(request, response);
    }
}
