<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.algonquin.ToiTalk.model.User" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Search Tutors</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Custom CSS -->
    <link rel="stylesheet" href="supplemental/styles.css">
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
        newInput.className = "form-control bg-dark text-white w-50";
        newInput.required = true;

        // Append the new input element to the div searchBlock
        searchBlock.appendChild(newInput);
    }

    function openNav() {
        document.getElementById("Sidenav").style.width = "250px";
    }

    function closeNav() {
        document.getElementById("Sidenav").style.width = "0";
    }
	</script>
</head>
<body>
<jsp:include page="supplemental/toolbar.jsp" />

    <div class="main-card card-large">
        <h2>Search for Tutors</h2>
        <div class="card-body mb-3">
            <form action="searchTutorServlet" id="searchForm" method="get">
                <div class="mb-3">
                    <label for="searchType" class="form-label">Search by:</label>
                    <select name="searchType" id="searchType" class="form-control bg-dark text-white w-50" onchange="toggleSearchInput()">
                        <option value="name">Name</option>
                        <option value="language">Language</option>
                    </select>
                </div>
                <div id="searchBlock" class="mb-3">
                    <input type="text" name="searchInput" id="searchInput" class="form-control bg-dark text-white w-50" placeholder="Enter name" required>
                </div>
                <div id="searchButton" class="d-grid">
                    <button type="submit" class="btn btn-primary text-white">Search</button>
                </div>
            </form>
        </div>

        <c:if test="${not empty tutorResults}">
            <div class="card-body">
                <ul>
                    <c:forEach var="tutor" items="${tutorResults}">
                        <div onclick="this.querySelector('form').submit();">
                            <form action="loadTutorProfileServlet" method="post" class="tutor-card">
                                <input type="hidden" name="tutorId" value="${tutor.getTutorID()}" />
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
                                <button type="submit" style="display:none;">Go to Profile</button>
                            </form>
                        </div>
                    </c:forEach>
                </ul>
            </div>
        </c:if>
    </div>
</body>
</html>