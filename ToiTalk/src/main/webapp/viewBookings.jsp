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
                                    <td>${booking.bookingID}</td>
                                    <td>${booking.studentName}</td>
                                    <td>${booking.tutorName}</td>
                                    <td>${booking.date}</td>
                                    <td>${booking.time}</td>
                                    <td>${booking.status}</td>
                                    <td>
                                        <c:if test="${booking.status eq 'PENDING'}">
                                            <form action="updateBooking" method="post" style="display: inline;">
                                                <input type="hidden" name="bookingId" value="${booking.bookingID}">
                                                <button type="submit" name="action" value="accept" class="btn btn-success btn-sm">Accept</button>
                                                <button type="submit" name="action" value="reject" class="btn btn-danger btn-sm">Reject</button>
                                            </form>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>
            <c:otherwise>
                <div class="alert alert-info">No bookings found.</div>
            </c:otherwise>
        </c:choose>
        
        <a href="dashboard.jsp" class="btn btn-primary">Back to Dashboard</a>
    </div>
</body>
</html>
