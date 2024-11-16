<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Book a Session</title>
    <style>
        table {
            width: 100%;
            border-collapse: collapse;
        }

        td {
            padding: 10px;
            text-align: center;
            border: 1px solid #ddd;
        }

        .open {
            background-color: #c8e6c9; /* soft green */
            cursor: pointer;
        }

        .booked, .null {
            background-color: #ffebee; /* soft red */
        }

        .confirmation {
            margin-top: 20px;
            display: none;
        }

        .confirmation strong {
            font-size: 1.2em;
        }

        .confirm-btn {
            padding: 10px 20px;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        .confirm-btn:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>

<h1>Book a Session with ${tutor.getUsername()}</h1>

<!-- Schedule Table -->
<table>
    <thead>
        <tr>
            <th>Monday</th>
            <th>Tuesday</th>
            <th>Wednesday</th>
            <th>Thursday</th>
            <th>Friday</th>
            <th>Saturday</th>
            <th>Sunday</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="hour" begin="0" end="23">
            <tr>
                <c:forEach var="day" begin="0" end="6">
                    <td>
                        <c:choose>
							<c:when test="${schedule[day][hour] == 'Open'}">
						        <!-- Open slot: clickable -->
						        <button type="button" class="open" onclick="showConfirmationForm(${day}, ${hour})">${hour}:00</button>
						    </c:when>
                            <c:when test="${schedule == 'Booked' or schedule == null}">
                                <!-- Booked or Null slot: non-clickable -->
                                <span class="booked">${hour}:00</span>
                            </c:when>
                        </c:choose>
                    </td>
                </c:forEach>
            </tr>
        </c:forEach>
    </tbody>
</table>

<!-- Confirmation Form -->
<div class="confirmation" id="confirmationForm">
    <h3>Confirm Booking</h3>
    <p><strong id="confirmDetails"></strong></p>
    <form action="confirmBookingServlet" method="POST">
        <input type="hidden" name="tutorId" value="${tutorId}">
        <input type="hidden" name="day" id="confirmDay">
        <input type="hidden" name="hour" id="confirmHour">
        <button type="submit" class="confirm-btn">Confirm</button>
    </form>
</div>

<script>
    // Function to display confirmation form with selected slot
    function showConfirmationForm(day, hour) {
        // Convert day index to actual weekday name
        var daysOfWeek = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"];
        var timeSlot = hour + ":00";
        
        // Update confirmation text
        document.getElementById("confirmDetails").innerText = "You are about to book " + daysOfWeek[day] + " at " + timeSlot;
        document.getElementById("confirmDay").value = day;
        document.getElementById("confirmHour").value = hour;

        // Show the confirmation form
        document.getElementById("confirmationForm").style.display = "block";
    }
</script>

</body>
</html>
