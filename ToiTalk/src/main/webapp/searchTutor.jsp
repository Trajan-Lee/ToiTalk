<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.algonquin.ToiTalk.model.User" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
<head>
	<style>
	    /* Style for the clickable tutor card */
	    .tutor-card {
	        display: block;
	        padding: 15px;
	        margin: 10px 0;
	        border: 1px solid #ccc;
	        border-radius: 8px;
	        text-decoration: none;
	        color: #333;
	        background-color: #f9f9f9;
	        transition: background-color 0.3s;
	    }
	
	    .tutor-card:hover {
	        background-color: #e0e0e0;
	    }
	
	    .tutor-card h3 {
	        margin: 0;
	        font-size: 1.2em;
	        color: #0056b3;
	    }
	
	    .tutor-card p {
	        margin: 4px 0;
	        font-size: 1em;
	        color: #555;
	    }
	</style>
    <meta charset="UTF-8">
    <title>Search Tutors</title>
    <script>
    function toggleSearchInput() {
        const searchType = document.getElementById("searchType").value;
        
        // Toggle visibility of name input and language select
        document.getElementById("nameInput").style.display = searchType === "name" ? "block" : "none";
        document.getElementById("languageSelect").style.display = searchType === "language" ? "block" : "none";
        
        // Update the "searchInput" name dynamically so that only one field is submitted
        if (searchType === "name") {
            document.getElementById("nameInput").name = "searchInput";  // Set name to searchInput
            document.getElementById("languageSelect").name = "";  // Remove name from language select
        } else {
            document.getElementById("nameInput").name = "";  // Remove name from name input
            document.getElementById("languageSelect").name = "searchInput";  // Set name to searchInput
        }
    }
    </script>
</head>
<body>
<h1>Search for Tutors</h1>

<form action="searchTutorServlet\	" method="get">
    <label for="searchType">Search by:</label>
    <select name="searchType" id="searchType" onchange="toggleSearchInput()">
        <option value="name">Name</option>
        <option value="language">Language</option>
    </select>
    
    <!-- Name input (visible when "name" is selected) -->
    <input type="text" name="searchInput" id="nameInput" placeholder="Enter name" required style="display: block;">
    
    <!-- Language dropdown (visible when "language" is selected) -->
    <select name="searchInput" id="languageSelect" style="display: none;">
		<c:forEach var="lang" items="${requestScope.langList}">
		    <option value="${lang}">${lang}</option>
		</c:forEach>
    </select>

    <button type="submit">Search</button>
</form>

<c:if test="${not empty tutorResults}">
    <ul>
		<c:forEach var="tutor" items="${tutorResults}">
		    <!-- Form for POST request to tutorProfile.jsp -->
		    <form action="loadTutorProfileServlet" method="post" class="tutor-card">
		        <!-- Hidden input field for tutor ID -->
		        <input type="hidden" name="tutorId" value="${tutor.getTutorID()}" />
		        
		        <!-- Tutor details displayed within the card -->
		        <div class="tutor-info">
		            <h2>${tutor.getUsername()}</h2>
		            <p>Languages: 
		                <c:forEach var="lang" items="${tutor.getLanguages()}">
		                    ${lang.getLangName()}<c:if test="${not empty fn:escapeXml(lang.getLangName())}">, </c:if>
		                </c:forEach>
		            </p>
		            <p>Experience: ${tutor.getExpYears()} years</p>
		        </div>
		        <!-- Submit button to submit the form -->
		        <button type="submit" style="display:none;">Go to Profile</button>
		    </form>
		</c:forEach>
    </ul>
</c:if>


</body>
</html>