
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ToiTalk Login</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Custom CSS -->
    <link rel="stylesheet" href="supplemental/styles.css">
</head>
<body>
    <div class=main-card>
        <h2>
            <span class="brand-title">Toi</span><span class="brand-subtitle">Talk</span>
        </h2>
        <h4 class="text-center mb-4">Login</h4>
            <!-- Error Message -->
	<c:if test="${not empty errorMessage}">
	    <div class="error-message">
	        ${errorMessage}
	    </div>
	</c:if>

        <form action="loginServlet" method="post">
            <!-- Username Input -->
            <div class="mb-3">
                <label for="email" class="form-label">Email</label>
                <input type="text" name="email" class="form-control bg-dark text-white" id="email" placeholder="email" autocomplete="email">
            </div>

            <!-- Password Input -->
            <div class="mb-3">
                <label for="password" class="form-label">Password</label>
                <input type="password" name="password" class="form-control bg-dark text-white" id="password" placeholder="Password" autocomplete="current-password">
            </div>


            <!-- Sign In Button -->
            <div class="d-grid">
                <button type="submit" class="btn btn-primary text-white">Sign In</button>
            </div>

            <!-- Extra Links -->
            <div class="text-center mt-4">
                <p>Don't have an account? <a href="loadRegisterServlet">Sign up here!</a></p>
            </div>
        </form>
    </div>

    <!-- Bootstrap JS (Optional) -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
