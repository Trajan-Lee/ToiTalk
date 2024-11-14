<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="com.algonquin.ToiTalk.model.User, com.algonquin.ToiTalk.model.Tutor" %>


<html>
<head>
    <title>User Profile</title>
</head>
<body>
    <h1>User Profile</h1>
    
    <p><strong>Username:</strong> ${user.getUsername()}</p>
    <p><strong>Email:</strong> ${user.getEmail()}</p>
    <p><strong>Type:</strong> ${user.getType()}</p>
    <p><strong>${user.getType()} Since:</strong> 
       <fmt:formatDate value="${user.getCreatetime()}" pattern="MMMM dd, yyyy" />
    </p>

    <c:choose>
        <!-- Student View -->
        <c:when test="${user.getType() == 'student'}">
            <button onclick="location.href='editProfile.jsp'">Edit Profile</button>
        </c:when>

        <!-- Tutor View -->
        <c:when test="${user.getType() == 'tutor'}">
            <p><strong>Bio:</strong> ${user.getBio()}</p>
				<p><strong>Languages:</strong>
				    <c:forEach var="lang" items="${user.languages}" varStatus="status">
				        ${lang.langName}<c:if test="${!status.last}">, </c:if>
				    </c:forEach>
				</p>
            <p><strong>Years of Experience:</strong> ${user.getExpYears()}</p>

            <button onclick="location.href='editProfile.jsp'">Edit Profile</button>
            <button onclick="location.href='editSchedule.jsp'">Edit Schedule</button>
        </c:when>
    </c:choose>
</body>
</html>
