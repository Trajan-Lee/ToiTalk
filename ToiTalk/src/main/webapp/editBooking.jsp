<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Booking</title>
    <style>
        .card {
            border: 1px solid #ccc;
            padding: 20px;
            margin: 20px;
        }
        .row {
            margin: 10px 0;
        }
        .label {
            font-weight: bold;
            display: inline-block;
            width: 150px;
        }
        .badge {
            padding: 5px 10px;
            border-radius: 4px;
            color: white;
        }
        .badge-warning { background-color: #ffc107; }
        .badge-success { background-color: #28a745; }
        .badge-danger { background-color: #dc3545; }
        .badge-secondary { background-color: #6c757d; }
        .btn {
            padding: 8px 16px;
            margin: 5px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        .btn-danger {
            background-color: #dc3545;
            color: white;
        }
        .btn-secondary {
            background-color: #6c757d;
            color: white;
        }
        .alert {
            padding: 15px;
            margin: 20px;
            border-radius: 4px;
        }
        .alert-danger {
            background-color: #f8d7da;
            border: 1px solid #f5c6cb;
            color: #721c24;
        }
    </style>
</head>
<body>
    <div>
        <h2>Booking Details</h2>
        
        <c:choose>
            <c:when test="${not empty booking}">
                <div class="card">
                    <div class="row">
                        <span class="label">Booking ID:</span>
                        <span>${booking.bookingID}</span>
                    </div>
                    <div class="row">
                        <span class="label">Student Name:</span>
                        <span>${booking.studentName}</span>
                    </div>
                    <div class="row">
                        <span class="label">Tutor Name:</span>
                        <span>${booking.tutorName}</span>
                    </div>
                    <div class="row">
                        <span class="label">Date:</span>
                        <span>${booking.date}</span>
                    </div>
                    <div class="row">
                        <span class="label">Time:</span>
                        <span>${booking.time}</span>
                    </div>
                    <div class="row">
                        <span class="label">Status:</span>
                        <span>
                            <c:choose>
                                <c:when test="${booking.status eq 'PENDING'}">
                                    <span class="badge badge-warning">${booking.status}</span>
                                </c:when>
                                <c:when test="${booking.status eq 'CONFIRMED'}">
                                    <span class="badge badge-success">${booking.status}</span>
                                </c:when>
                                <c:when test="${booking.status eq 'CANCELLED'}">
                                    <span class="badge badge-danger">${booking.status}</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge badge-secondary">${booking.status}</span>
                                </c:otherwise>
                            </c:choose>
                        </span>
                    </div>
                    
                    <div style="margin-top: 20px;">
                        <c:if test="${booking.status ne 'CANCELLED'}">
                            <form action="cancelBooking" method="post" style="display: inline;">
                                <input type="hidden" name="bookingId" value="${booking.bookingID}">
                                <button type="submit" class="btn btn-danger" 
                                        onclick="return confirm('Are you sure you want to cancel this booking?')">
                                    Cancel Booking
                                </button>
                            </form>
                        </c:if>
                        <a href="viewBookings.jsp" class="btn btn-secondary">Back to Bookings</a>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="alert alert-danger">
                    Booking not found or no booking ID provided.
                    <br>
                    <a href="viewBookings.jsp" class="btn btn-secondary" style="margin-top: 10px;">Back to Bookings</a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>
