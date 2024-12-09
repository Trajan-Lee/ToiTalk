<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>



<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Change Feedback</title>
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
    </style>
</head>
<body>
    <h2>Change Feedback <c:out value="${feedbackExists}"/>
    </h2>
    <form action="saveFeedbackServlet" method="post">
        <input type="hidden" name="bookingId" value="${bookingId}">
        <input type="hidden" name="tutorId" value="${tutorId}">
        <input type="hidden" name="feedbackExists" value="${feedbackExists}">

		<!-- uses workaround from LoadFeedback to start from 5 down to 1 -->
        <div class="star-rating">
            <c:forEach var="i" items="${starList}">
				<input type="radio" id="star${i}" name="rating" value="${i}" 
					<c:if test="${feedbackExists && feedback.rating == i}">checked</c:if> required/>
                <label for="star${i}" title="${i} stars">&#9733;</label>
            </c:forEach>
        </div>

        <div>
            <label for="comment">Comment:</label>
            <textarea id="comment" name="comment" rows="4" cols="50" required>${feedbackExists ? feedback.comment : ''}</textarea>
        </div>

        <button type="submit">Save Feedback</button>
    </form>
    
        
</body>
</html>