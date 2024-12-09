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
    <p><strong>Type:</strong> <span class="user-type">${user.getType()}</span></p>
    <p><strong><span class="user-type">${user.getType()}</span> Since:</strong> 
       <fmt:formatDate value="${user.getCreatetime()}" pattern="MMMM dd, yyyy" />
    </p>

    <c:choose>
        <c:when test="${user.getType() == 'student'}">
            <button onclick="location.href='editProfile.jsp'">Edit Profile</button>
        </c:when>
        <c:when test="${user.getType() == 'tutor'}">
            <p><strong>Bio:</strong> ${user.getBio()}</p>
				<p><strong>Languages:</strong>
				    <c:forEach var="lang" items="${user.getLanguages()}" varStatus="status">
				        ${lang.langName}<c:if test="${!status.last}">, </c:if>
				    </c:forEach>
				</p>
            <p><strong>Years of Experience:</strong> ${user.getExpYears()}</p>

            <button onclick="location.href='loadEditProfileServlet'">Edit Profile</button>
            <button onclick="location.href='loadEditScheduleServlet'">Edit Schedule</button>
        </c:when>
    </c:choose>
</body>

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

</html>
