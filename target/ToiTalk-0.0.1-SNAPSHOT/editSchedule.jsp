<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
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

        .soft-red {
            background-color: #f8d7da; /* Soft red */
        }

        .soft-green {
            background-color: #d4edda; /* Soft green */
        }

        .soft-blue {
            background-color: #d1ecf1; /* Soft blue */
        }

        .day-header {
            font-weight: bold;
            text-align: center;
        }
    </style>
</head>
<body>
    <h2>Edit Schedule</h2>
    <form action="submitScheduleServlet" method="POST">
        <button type="submit">Save Schedule</button>
        <table class="schedule-table">
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
                        <td id="cell-${day}-${hour}" class="${empty tutorSchedule[day][hour] ? 'soft-red' : (tutorSchedule[day][hour] == 'Open' ? 'soft-green' : 'soft-blue')}" 
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
        <br>
    </form>

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
                cell.classList.remove("soft-green");
                cell.classList.add("soft-red");
                hiddenInput.remove();
            } else if (currentValue === "") {
                // Set to "Open" (soft green)
                cell.innerText = "Open";
                cell.classList.remove("soft-red");
                cell.classList.add("soft-green");
                
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
