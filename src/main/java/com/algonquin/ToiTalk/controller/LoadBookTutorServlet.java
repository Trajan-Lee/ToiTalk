package com.algonquin.ToiTalk.controller;

import com.algonquin.ToiTalk.DAO.ScheduleDAO;
import com.algonquin.ToiTalk.DAO.UserDAO;
import com.algonquin.ToiTalk.model.Schedule;
import com.algonquin.ToiTalk.model.Tutor;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet("/loadBookTutorServlet")
public class LoadBookTutorServlet extends HttpServlet {
    private UserDAO userDAO;
    private ScheduleDAO scheduleDAO;
    Connection connection;

    @Override
	public void init() throws ServletException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/toitalk", "user1", "password1");
			this.userDAO = new UserDAO(connection);
			this.scheduleDAO = new ScheduleDAO(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
	        throw new ServletException("MySQL JDBC Driver not found", e);
		}
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the tutorId from the request parameter
        String tutorIDParam = request.getParameter("tutorId");

        // Check if tutorId is present and valid
        if (tutorIDParam != null) {
            try {
                int tutorID = Integer.parseInt(tutorIDParam); // Convert to int
                Tutor tutor = userDAO.getTutorObjByID(tutorID); // Retrieve Tutor object by ID

                if (tutor != null) {
                	// get weekly schedule list
                	LocalDate today = LocalDate.now();
                    int inWeeks = today.getDayOfWeek().getValue() == 7 ? 4 : 5; // if today is Sunday, get next week's schedule
                	List<Schedule> weeklySchedules = scheduleDAO.getWeeklySchedules(tutor.getSchedule(), inWeeks);
                	// get available days from weekly schedules
                	Boolean[][] availableDays = getAvailableDays(weeklySchedules, inWeeks);  
                	// Convert List<Schedule> to JSON
                	Gson gson = new Gson();
					String weeklySchedulesJson = gson.toJson(weeklySchedules);
					String[][] dateArray = generateDateArray(availableDays);

					// Set attributes for the tutor booking page
					request.setAttribute("tutor", tutor);
                	request.setAttribute("weeklySchedulesJson", weeklySchedulesJson);
                	request.setAttribute("dateArray", dateArray);
                    request.setAttribute("availableDays", availableDays);
                    request.setAttribute("tutorId", tutorID);
                    // Forward to the tutor profile JSP page
                    request.getRequestDispatcher("/bookTutor.jsp").forward(request, response);
                    return;
                } else {
                    // Tutor not found, set an error message
                    request.setAttribute("errorMessage", "Tutor not found.");
                }
            } catch (NumberFormatException e) {
                // Invalid tutorId format, set an error message
                request.setAttribute("errorMessage", "Invalid tutor ID format.");
            }
        } else {
            // tutorId parameter is missing
            request.setAttribute("errorMessage", "Missing tutor ID.");
        }

        // If an error occurs, forward to an error page or display an error message
        request.getRequestDispatcher("/error.jsp").forward(request, response);
    }
    
	public String[][] filterScheduleList(List<Schedule> inList, Schedule originalSchedule) {
    	String[][] outList = originalSchedule.getArrSchedule();
		for (Schedule s : inList) {
			String[][] temp = s.getArrSchedule();
			for (int day = 0; day < 7; day++) {
				for (int hour = 0; hour < 24; hour++) {
					if (temp[day][hour] != null) {
						outList[day][hour] = temp[day][hour];
					}
				}
			}
		}
		return outList;
    }
	
    public Boolean[][] getAvailableDays(List<Schedule> inList, int inWeeks) {
        Boolean[][] outList = new Boolean[5][7];
        LocalDate today = LocalDate.now();
        LocalDate thisWeekStart = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate endDate = today.plusWeeks(4).plusDays(1);
        Boolean available = false;

        for (int week = 0; week < inWeeks; week++) {
            Schedule weekSchedule = inList.get(week);
            String[][] temp = weekSchedule.getArrSchedule();
            for (int day = 0; day < 7; day++) {
                LocalDate currentDate = thisWeekStart.plusDays(week * 7 + day);
                System.out.println("currentDate: " + currentDate);
            	available = false;
                // ensure date is within the next 4 weeks
                if (currentDate.isAfter(today) && currentDate.isBefore(endDate)) {
                    for (int hour = 0; hour < 24; hour++) {
                        if (temp[day][hour] != null) {
                            available = true;
                            break;
                        }
                    }
                    outList[week][day] = available;
                } else {
                    outList[week][day] = false;
                }
            }
        }
        return outList;
    }
    public String[][] generateDateArray(Boolean[][] availability) {
        String[][] dateArray = new String[5][7];
        LocalDate today = LocalDate.now();
        LocalDate thisWeekStart = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate endDate = today.plusWeeks(4).plusDays(1);

        for (int week = 0; week < 5; week++) {
            for (int day = 0; day < 7; day++) {
                LocalDate currentDate = thisWeekStart.plusDays(week * 7 + day);

                // Assign "NA" if the date is out of range, otherwise assign the date
                if (!currentDate.isAfter(today) || currentDate.isAfter(endDate)) {
                    dateArray[week][day] = "NA";
                } else {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");
                    String formattedDate = currentDate.format(formatter);
                    dateArray[week][day] = formattedDate; // MM-DD
                }
            }
        }

        return dateArray;
    }
}
