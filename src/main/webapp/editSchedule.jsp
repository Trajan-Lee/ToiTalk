
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Schedule</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="supplemental/styles.css">
    <style>
        .schedule-table {
            width: 100%;
            border-collapse: collapse;
        }

        .schedule-table th, .schedule-table td {
            padding: 8px;
            text-align: center;
        }

        .schedule-table tr {
            border-bottom: 2px solid black; /* Bold horizontal lines */
        }

        .schedule-table td {
            border-left: 1px solid lightgray; /* Soft vertical lines */
        }

        .schedule-table th {
            border-left: 1px solid lightgray; /* Soft vertical lines in headers */
        }

        /* Remove the left border of the first column to avoid double borders */
        .schedule-table tr td:first-child,
        .schedule-table tr th:first-child {
            border-left: none;
        }

        .schedule-table td {
            width: 50px;
            height: 30px;
            text-align: center;
            cursor: pointer;
        }

        .red {
            background-color: #7A1212 !important; /* Soft red */
        }

        .green {
            background-color: #0F3D13 !important; /* Soft green */
        }

        .day-header {
            font-weight: bold;
            text-align: center;
        }
        
        .available {
		    background-color: #0F3D13 !important; /* Dark green */
		    cursor: pointer;
		}
		
		.unavailable {
		    background-color: #7A1212 !important; /* Dark Red */
		}
        
    </style>
</head>
<body>
<jsp:include page="supplemental/toolbar.jsp" />
    <div class="main-card card-large">
        <h2 class="text-center mb-4"><span class="brand-title">Toi</span><span class="brand-subtitle">Talk</span></h2>
        <h4 class="text-center mb-4">Edit Schedule</h4>

        <form action="submitScheduleServlet" method="POST">
            <button type="submit" class="btn btn-primary text-white mb-3">Save Schedule</button>
            <table class="schedule-table table table-dark">
                <!-- Day headers -->
                <tr>
                    <th class="day-header">Hour</th>
                    <th class="day-header">Mon</th>
                    <th class="day-header">Tue</th>
                    <th class="day-header">Wed</th>
                    <th class="day-header">Thu</th>
                    <th class="day-header">Fri</th>
                    <th class="day-header">Sat</th>
                    <th class="day-header">Sun</th>
                </tr>

                <!-- Hour rows -->
                <c:forEach var="hour" begin="0" end="23">
                    <tr>
                        <td>${hour}:00</td>
                        <c:forEach var="day" begin="0" end="6">
                            <td id="cell-${day}-${hour}" class="${empty tutorSchedule[day][hour] ? '' : (tutorSchedule[day][hour] == 'Open' ? 'green' : 'soft-blue')}" 
                                onclick="toggleSchedule('${day}', '${hour}')">
                                <c:choose>
                                    <c:when test="${empty tutorSchedule[day][hour]}"> </c:when>
                                    <c:when test="${tutorSchedule[day][hour] == 'Open'}">
                                        Open
                                        <input type="hidden" name="schedule-${day}-${hour}" id="hidden-schedule-${day}-${hour}" value="Open" />
                                    </c:when>
                                    <c:otherwise>
                                        Booked
                                        <input type="hidden" name="schedule-${day}-${hour}" id="hidden-schedule-${day}-${hour}" value="Booked" />
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </c:forEach>
                    </tr>
                </c:forEach>
            </table>
        </form>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // JavaScript to handle toggling of 'Open' and null values
        function toggleSchedule(day, hour) {
            var cell = document.getElementById("cell-" + day + "-" + hour);
            var hiddenInput = document.getElementById("hidden-schedule-" + day + "-" + hour);
            var currentValue = cell.innerText;

            // Toggle the value based on current state
            if (currentValue === "Open") {
                // Set to null (empty cell, soft red)
                cell.innerText = "";
                cell.classList.remove("green");
                hiddenInput.remove();
            } else if (currentValue === "") {
                // Set to "Open" (soft green)
                cell.innerText = "Open";
                cell.classList.add("green");

                //create new input element
                const newHiddenInput = document.createElement("input");
                newHiddenInput.type = "hidden";
                newHiddenInput.id = "hidden-schedule-" + day + "-" + hour;
                newHiddenInput.name = "schedule-" + day + "-" + hour;
                newHiddenInput.value = "Open";
                cell.appendChild(newHiddenInput);
            }
        }
    </script>
</body>
</html>
