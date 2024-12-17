
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="com.algonquin.ToiTalk.model.User, com.algonquin.ToiTalk.model.Tutor" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>User Profile</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="supplemental/styles.css">
    <style>
        .main-card {
            display: flex;
            flex-direction: column;
            align-items: center;
            text-align: center;
        }
    </style>
</head>
<body>
<jsp:include page="supplemental/toolbar.jsp" />
    <div class="main-card card-large">
        <h2 class="text-center mb-4"><span class="brand-title">Toi</span><span class="brand-subtitle">Talk</span></h2>
        <h4 class="text-center mb-4">User Profile</h4>

        <div class="card-body">
            <p><strong>Username:</strong> ${user.getUsername()}</p>
            <p><strong>Email:</strong> ${user.getEmail()}</p>
            <p><strong>Type:</strong> <span class="user-type">${user.getType()}</span></p>
            <p><strong><span class="user-type">${user.getType()}</span> Since:</strong> 
               <fmt:formatDate value="${user.getCreatetime()}" pattern="MMMM dd, yyyy" />
            </p>

            <c:choose>
                <c:when test="${user.getType() == 'student'}">
                    <button onclick="location.href='editProfile.jsp'" class="btn btn-primary text-white">Edit Profile</button>
                </c:when>
                <c:when test="${user.getType() == 'tutor'}">
                    <p><strong>Bio:</strong> ${user.getBio()}</p>
                    <p><strong>Languages:</strong>
                        <c:forEach var="lang" items="${user.getLanguages()}" varStatus="status">
                            ${lang.langName}<c:if test="${!status.last}">, </c:if>
                        </c:forEach>
                    </p>
                    <p><strong>Years of Experience:</strong> ${user.getExpYears()}</p>

                    <button onclick="location.href='loadEditProfileServlet'" class="btn btn-primary text-white">Edit Profile</button>
                    <button onclick="location.href='loadEditScheduleServlet'" class="btn btn-primary text-white">Edit Schedule</button>
                </c:when>
            </c:choose>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Capitalize the first letter of the user type, uses the Span
        const userTypeElements = document.querySelectorAll('.user-type');

        // Iterate through all selected elements
        userTypeElements.forEach(element => {
            // Capitalize the first letter of the text content
            const capitalizedText = element.textContent.charAt(0).toUpperCase() + element.textContent.slice(1);

            // Update the text content of the element
            element.textContent = capitalizedText;
        });
    </script>
</body>
</html>
