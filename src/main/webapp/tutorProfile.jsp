
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tutor Profile</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="supplemental/styles.css">
</head>
<body>
<jsp:include page="supplemental/toolbar.jsp" />
    <div class="main-card card-medium">
        <h2 class="text-center mb-4"><span class="brand-title">Toi</span><span class="brand-subtitle">Talk</span></h2>
        <h4 class="text-center mb-4">Tutor Profile</h4>

        <div class="card-body">
            <h2 class="text-center">${tutor.getUsername()}</h2>

            <div class="mb-3">
                <strong>Languages:</strong>
                <c:forEach var="lang" items="${tutor.getLanguages()}">
                    <span>${lang.getLangName()}</span><c:if test="${not empty fn:escapeXml(lang.getLangName())}">, </c:if>
                </c:forEach>
            </div>

            <div class="mb-3">
                <strong>Biography:</strong>
                <p>${tutor.getBio()}</p>
            </div>

            <div class="mb-3">
                <strong>Rating:</strong> ${tutor.getRating()} / 5
            </div>

            <div class="mb-3">
                <strong>Years of Experience:</strong> ${tutor.getExpYears()} years
            </div>

            <form action="loadBookTutorServlet" method="POST" class="d-grid">
                <input type="hidden" name="tutorId" value="${tutor.getTutorID()}">
                <button type="submit" class="btn btn-primary text-white">Book a Session</button>
            </form>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
