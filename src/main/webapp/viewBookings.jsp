
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>View Bookings</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="supplemental/styles.css">
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

        .btn.feedback {
            background-color: #008CBA;
            color: white;
            padding: 5px 10px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
    </style>
</head>
<body>
<jsp:include page="supplemental/toolbar.jsp" />
    <div class="main-card card-xlarge">
        <h4 class="text-center mb-4">View Bookings</h4>

        <h3>Filter by Status:</h3>
        <form id="filterForm" method="get" action="viewBookingServlet">
            <label>
                <input type="radio" name="status" value="scheduled" onclick="document.getElementById('filterForm').submit();" <c:if test="${status == 'scheduled'}">checked</c:if>> Scheduled
            </label>
            <label>
                <input type="radio" name="status" value="completed" onclick="document.getElementById('filterForm').submit();" <c:if test="${status == 'completed'}">checked</c:if>> Completed
            </label>
            <label>
                <input type="radio" name="status" value="canceled" onclick="document.getElementById('filterForm').submit();" <c:if test="${status == 'canceled'}">checked</c:if>> Canceled
            </label>
            <label>
                <input type="radio" name="status" value="all" onclick="document.getElementById('filterForm').submit();" <c:if test="${status == 'all'}">checked</c:if>> All
            </label>
        </form>

        <br>
        <c:if test="${user.getType() == 'student'}">
            <label for="searchInput">Search by Tutor Name:</label>
        </c:if>
        <c:if test="${user.getType() == 'tutor'}">
            <label for="searchInput">Search by Student Name:</label>
        </c:if>
        <input type="text" id="searchInput" placeholder="Search..." onkeyup="filterBookings()" class="form-control bg-dark text-white w-50 mb-3">

        <c:if test="${not empty bookings && status != null}">
            <div class="table-responsive">
                <table class="table table-dark table-striped" id="bookingsTable">
                    <thead>
                        <tr>
                            <th>Booking ID</th>
                            <th>Student Name</th>
                            <th>Tutor Name</th>
                            <th>Date</th>
                            <th>Status</th>
                            <th>Actions</th>
                            <th>DevTool Change Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="booking" items="${bookings}">
                            <tr>
                                <td>${booking.getBookingID()}</td>
                                <td>${booking.getStudentName()}</td>
                                <td>${booking.getTutorName()}</td>
                                <td>${booking.getDate()}</td>
                                <td>${booking.getStatus()}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${booking.getStatus() == 'scheduled'}">
                                            <form action="cancelBookingServlet" method="post" id="cancelForm-${booking.getBookingID()}" style="display: inline;">
                                                <input type="hidden" name="bookingId" value="${booking.getBookingID()}">
                                                <button type="button" onclick="showConfirm(${booking.getBookingID()})" class="btn cancel">Cancel</button>
                                                <button type="submit" id="confirm-${booking.getBookingID()}" style="display: none;" class="btn confirm">Confirm</button>
                                            </form>
                                        </c:when>
                                        <c:when test="${booking.getStatus() == 'completed' && user.getType() == 'student'}">
                                            <form action="loadFeedbackServlet" method="post" id="feedbackForm-${booking.getBookingID()}" style="display: inline;">
                                                <input type="hidden" name="bookingId" value="${booking.getBookingID()}">
                                                <input type="hidden" name="tutorId" value="${booking.getTutorID()}">
                                                <c:if test="${booking.getFeedback() == false}">
                                                    <button type="submit" class="btn feedback">Leave Feedback</button>
                                                    <input type="hidden" name="feedbackExists" value="false">
                                                </c:if>
                                                <c:if test="${booking.getFeedback() == true}">
                                                    <button type="submit" class="btn feedback">Change Feedback</button>
                                                    <input type="hidden" name="feedbackExists" value="true">
                                                </c:if>
                                            </form>
                                        </c:when>
                                        <c:otherwise>
                                            <!-- No action buttons for canceled bookings or completed bookings for tutors -->
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <form action="changeBookingStatusServlet" method="post" id="devToolChangeStatusForm-${booking.getBookingID()}" style="display: inline;">
                                        <input type="hidden" name="bookingId" value="${booking.getBookingID()}">
                                        <select name="changeStatus" class="form-control bg-dark text-white">
                                            <option value="scheduled">Scheduled</option>
                                            <option value="completed">Completed</option>
                                            <option value="canceled">Canceled</option>
                                        </select>
                                        <button type="submit" class="btn confirm">Change Status</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>
        <c:if test="${empty bookings && status != null}">
            <div>No bookings found.</div>
        </c:if>
        <c:if test="${status == null}">
            <div>Select a status to view bookings.</div>
        </c:if>

    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function showConfirm(bookingId) {
            // Hide all other confirm buttons first
            document.querySelectorAll('[id^="confirm-"]').forEach(button => {
                button.style.display = 'none';
            });
            // Show the confirm button for this booking
            document.getElementById('confirm-' + bookingId).style.display = 'inline';
        }

        function filterBookings() {
            const input = document.getElementById('searchInput');
            const filter = input.value.toLowerCase();
            const table = document.getElementById('bookingsTable');
            const rows = table.getElementsByTagName('tr');
            const userType = '${user.getType()}';
            const filterColumn = userType === 'tutor' ? 1 : 2; // 1 for student name, 2 for tutor name

            for (let i = 1; i < rows.length; i++) {
                const cells = rows[i].getElementsByTagName('td');
                const cell = cells[filterColumn];
                if (cell) {
                    const textValue = cell.textContent || cell.innerText;
                    if (textValue.toLowerCase().indexOf(filter) > -1) {
                        rows[i].style.display = '';
                    } else {
                        rows[i].style.display = 'none';
                    }
                }
            }
        }
    </script>
</body>
</html>
