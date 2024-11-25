<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.algonquin.ToiTalk.model.Booking" %>
<%@ page import="com.algonquin.ToiTalk.model.User" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>View Bookings</title>
    <style>
        .btn.cancel {
            background-color: #ff4444;
            color: white;
            padding: 5px 10px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        .btn.confirm {
            background-color: #4CAF50;
            color: white;
            padding: 5px 10px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            margin-left: 5px;
        }
    </style>
</head>
<body>
    <div>
        <h2>All Bookings</h2>
        
        <c:choose>
            <c:when test="${not empty bookings}">
                <div class="table-responsive">
                    <table class="table table-striped">
                        <thead>
                            <tr>
                                <th>Booking ID</th>
                                <th>Student Name</th>
                                <th>Tutor Name</th>
                                <th>Date</th>
                                <th>Time</th>
                                <th>Status</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="booking" items="${bookings}">
                                <tr>
                                    <td>${booking.getStudentName()}</td>
                                    <td>${booking.getTutorName()}</td>
                                    <td>${booking.getDate()}</td>
                                    <td>${booking.getTime()}</td>
                                    <td>${booking.getStatus()}</td>
                                    <td>
                                        <form action="cancelBooking" method="post" id="cancelForm-${booking.getBookingID()}" style="display: inline;">
                                            <input type="hidden" name="bookingId" value="${booking.getBookingID()}">
                                            <button type="button" onclick="showConfirm(booking.getBookingID())" class="btn cancel">Cancel</button>
                                            <button type="submit" id="confirm-${booking.getBookingID()}" style="display: none;" class="btn confirm">Confirm</button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>
            <c:otherwise>
                <div>No bookings found.</div>
            </c:otherwise>
        </c:choose>
        
        <a href="dashboard.jsp">Back to Dashboard</a>
    </div>
    <script>
        function showConfirm(bookingId) {
            // Hide all other confirm buttons first
            document.querySelectorAll('[id^="confirm-"]').forEach(button => {
                button.style.display = 'none';
            });
            // Show the confirm button for this booking
            document.getElementById('confirm-' + bookingId).style.display = 'inline';
        }
    </script>
</body>
</html>
