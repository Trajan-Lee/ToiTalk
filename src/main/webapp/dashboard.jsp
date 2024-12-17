
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ToiTalk Dashboard</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Custom CSS -->
    <link rel="stylesheet" href="supplemental/styles.css">

</head>
<body>
<jsp:include page="supplemental/toolbar.jsp" />

    <div class="main-card card-large">
        <h2>Welcome to Your Dashboard</h2>
        <div class="d-flex flex-column align-items-center">
            <div class="d-flex justify-content-center mb-2">
                <!-- View Profile Button -->
                <button class="btn btn-primary text-white mx-2" onclick="location.href='loadProfileServlet'">View Profile</button>

                <!-- View Booking Button -->
                <button class="btn btn-primary text-white mx-2" onclick="location.href='viewBookingServlet'">View Booking</button>

                <!-- Search Tutors Button -->
                <c:if test="${user != null && user.type == 'student'}">
                    <button class="btn btn-primary text-white mx-2" onclick="location.href='loadSearchTutorServlet'">Search Tutors</button>
                </c:if>
            </div>
            <div class="d-flex justify-content-center">
                <!-- Log-out Button -->
                <button class="btn btn-primary text-white" onclick="location.href='logoutServlet'">Log-out</button>
            </div>
        </div>
    </div>
</body>
</html>
