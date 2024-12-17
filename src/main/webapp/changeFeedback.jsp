
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Change Feedback</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="supplemental/styles.css">
    <style>
        .star-rating {
            direction: rtl;
            display: inline-block;
            padding: 20px;
        }
        .star-rating input[type="radio"] {
            display: none;
        }
        .star-rating label {
            color: #bbb;
            font-size: 18px;
            padding: 0;
            cursor: pointer;
            transition: all 0.3s ease-in-out;
        }
        .star-rating input[type="radio"]:checked ~ label {
            color: #f2b600;
        }
        .star-rating label:hover,
        .star-rating label:hover ~ label {
            color: #f2b600;
        }
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
    <div class="main-card card-medium">
		<h4 class="text-center mb-4">Change Feedback</h4>

        <form action="saveFeedbackServlet" method="post" class="w-100">
            <input type="hidden" name="bookingId" value="${bookingId}">
            <input type="hidden" name="tutorId" value="${tutorId}">
            <input type="hidden" name="feedbackExists" value="${feedbackExists}">

            <div class="star-rating">
                <c:forEach var="i" items="${starList}">
                    <input type="radio" id="star${i}" name="rating" value="${i}" <c:if test="${feedbackExists && feedback.rating == i}">checked</c:if> required/>
                    <label for="star${i}" title="${i} stars">&#9733;</label>
                </c:forEach>
            </div>

            <div class="mb-3">
                <label for="comment" class="form-label">Comment:</label>
                <textarea id="comment" name="comment" class="form-control bg-dark text-white w-75 mx-auto" rows="4" required>${feedbackExists ? feedback.comment : ''}</textarea>
            </div>

            <div class="d-grid w-50 mx-auto">
                <button type="submit" class="btn btn-primary text-white">Save Feedback</button>
            </div>
        </form>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
