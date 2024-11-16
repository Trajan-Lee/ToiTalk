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
    // Convert the `allLang` JSON attribute from the server into a JavaScript array
	const languages = JSON.parse('<c:out value="${allLang}" escapeXml="false" />');
    
    function toggleSearchInput() {
        const searchType = document.getElementById("searchType").value;
        const searchBlock = document.getElementById("searchBlock");

        // Remove any existing search input elements
        const existingInput = document.getElementById("searchInput");
        if (existingInput) {
            searchBlock.removeChild(existingInput);
        }

        // Create new input element based on search type
        let newInput;
        if (searchType === "name") {
            // Create a text input for searching by name
            newInput = document.createElement("input");
            newInput.type = "text";
            newInput.placeholder = "Enter name";
        } else if (searchType === "language") {
            // Create a select dropdown for searching by language
            newInput = document.createElement("select");

            // Dynamically add options to the select
            languages.forEach(lang => {
                const option = document.createElement("option");
                option.value = lang;
                option.textContent = lang;
                newInput.appendChild(option);
            });
        }

        // Set common attributes for the new input element
        newInput.name = "searchInput";
        newInput.id = "searchInput";
        newInput.required = true;

        // Append the new input element to the div searchBlock
        searchBlock.appendChild(newInput);
    }

    </script>
</head>
<body>
<h1>Search for Tutors</h1>

<form action="searchTutorServlet" id="searchForm" method="get">
    <label for="searchType">Search by:</label>
    <select name="searchType" id="searchType" onchange="toggleSearchInput()">
        <option value="name">Name</option>
        <option value="language">Language</option>
    </select>
	<div id="searchBlock">
    	<input type="text" name="searchInput" id="searchInput" placeholder="Enter name" required style="display: block;">
	</div>

	<div id="searchButton">
		<button type="submit">Search</button>
	</div>

</form>

<c:if test="${not empty tutorResults}">
    <ul>
        <c:forEach var="tutor" items="${tutorResults}">
            <div onclick="this.querySelector('form').submit();">
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
                        <p>Rating: ${tutor.getRating()}</p>
                        <p>Experience: ${tutor.getExpYears()} years</p>
                    </div>
                    <!-- Hidden submit button (no need to show this button since the div triggers submit) -->
                    <button type="submit" style="display:none;">Go to Profile</button>
                </form>
            </div>
        </c:forEach>
    </ul>
</c:if>



</body>
</html>