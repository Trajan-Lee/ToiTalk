<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
        
        <!-- Search Tutors Button -->
        <button class="dashboard-btn" onclick="location.href='loadSearchTutorServlet'">Search Tutors</button>
        
        <!-- Log-out Button -->
        <button class="dashboard-btn" onclick="location.href='logoutServlet'">Log-out</button>
    </div>

</body>
</html>
