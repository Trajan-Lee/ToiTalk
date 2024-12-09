<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard</title>
    <style>
        /* Basic styling for the dashboard */
        body {
            font-family: Arial, sans-serif;
            text-align: center;
            padding: 50px;
        }

        h1 {
            color: #333;
        }

        .dashboard-btn {
            font-size: 18px;
            padding: 10px 20px;
            margin: 10px;
            cursor: pointer;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 5px;
            transition: background-color 0.3s ease;
        }

        .dashboard-btn:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>

    <h1>Welcome to Your Dashboard</h1>

    <div>
        <!-- View Profile Button -->
        <button class="dashboard-btn" onclick="location.href='loadProfileServlet'">View Profile</button>
        
		<!-- View Booking Button -->
		<button class="dashboard-btn" onclick="location.href='viewBookingServlet'">View Booking</button>
		        
        <!-- Search Tutors Button -->
        <c:if test="${user != null && user.type == 'student'}">
            <button class="dashboard-btn" onclick="location.href='loadSearchTutorServlet'">Search Tutors</button>
        </c:if>
        
    </div>
    <div>
            <!-- Log-out Button -->
        <button class="dashboard-btn" onclick="location.href='logoutServlet'">Log-out</button>
        
    </div>
    

</body>
</html>
