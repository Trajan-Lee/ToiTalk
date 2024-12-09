<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Sign In</title>
        <style>
        .error-message {
            color: red;
            font-weight: bold;
            margin-top: 20px;
        }
    </style>
</head>
<body>
    <h2>Sign In to ToiTalk</h2>
    
    <!-- Error Message -->
	<c:if test="${not empty errorMessage}">
	    <div class="error-message">
	        ${errorMessage}
	    </div>
	</c:if>
    
    <form action="loginServlet" method="post">
        <label for="username">Email:</label>
        <input type="text" id="username" name="email" required><br>

        <label for="password">Password:</label>
        <input type="password" id="password" name="password" required><br>

        <input type="submit" value="Sign In">
        
		<button onclick="location.href='loadRegisterServlet'" type="button">Create New User</button>
        
    </form>
    

</body>
</html>