<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tutor Profile</title>
    <style>
        .profile-card {
            border: 1px solid #ddd;
            padding: 20px;
            border-radius: 8px;
            width: 300px;
            margin: 0 auto;
            box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
        }

        .profile-card h2 {
            text-align: center;
        }

        .profile-card .languages, .profile-card .bio, .profile-card .rating, .profile-card .experience {
            margin-bottom: 10px;
        }

        .profile-card .languages span {
            margin-right: 10px;
        }

        .book-btn {
            display: block;
            margin: 20px auto;
            padding: 10px 20px;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 5px;
            text-align: center;
            text-decoration: none;
        }

        .book-btn:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>
	<c:if test="${not empty param.tutorId}">
    	<c:set var="tutorId" value="${param.tutorId}" />
	</c:if>
    <div class="profile-card">
        <h2>${tutor.getUsername()}</h2>

        <!-- Display tutor's languages -->
        <div class="languages">
            <strong>Languages:</strong>
            <c:forEach var="lang" items="${tutor.getLanguages()}">
                <span>${lang.getLangName()}</span><c:if test="${not empty fn:escapeXml(lang.getLangName())}">, </c:if>
            </c:forEach>
        </div>

        <!-- Display tutor's bio -->
        <div class="bio">
            <strong>Biography:</strong>
            <p>${tutor.getBio()}</p>
        </div>

        <!-- Display tutor's rating -->
        <div class="rating">
            <strong>Rating:</strong> ${tutor.getRating()} / 5
        </div>

        <!-- Display tutor's years of experience -->
        <div class="experience">
            <strong>Years of Experience:</strong> ${tutor.getExpYears()} years
        </div>

        <!-- Form to book a session with the tutor using POST -->
        <form action="loadBookTutorServlet" method="POST">
            <input type="hidden" name="tutorId" value="${tutor.getTutorID()}">
            <button type="submit" class="book-btn">Book a Session</button>
        </form>
    </div>

</body>
</html>
