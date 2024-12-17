
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Book a Session</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="supplemental/styles.css">
    <style>
        table {
            width: 100%;
            border-collapse: collapse;
            table-layout: fixed; 
        }

        td {
            padding: 10px;
            text-align: center;
            border: 1px solid #ddd;
        }
        
		.available {
		    background-color: #0F3D13 !important; /* Dark green */
		    cursor: pointer;
		}
		
		.unavailable {
		    background-color: #7A1212 !important; /* Dark Red */
		}

        .day-schedule {
            margin-top: 20px;
            display: none;
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
        .error-message {
            color: red;
            font-weight: bold;
            margin-top: 20px;
        }
        #weeks, .confirm-btn {
            display: none;
        }
    </style>
</head>
<body>
<jsp:include page="supplemental/toolbar.jsp" />
    <div class="main-card card-large">
        <h2 class="text-center mb-4"><span class="brand-title">Toi</span><span class="brand-subtitle">Talk</span></h2>
        <h4 class="text-center mb-4">Book a Session with ${tutor.getUsername()}</h4>

        <c:if test="${not empty message}">
            <div class="error-message">
                ${message}
            </div>
        </c:if>

        <table class="table table-dark">
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
                <c:set var="count" value="1"/>
                <c:forEach var="week" begin="0" end="4">
                    <tr>
                        <c:forEach var="day" begin="0" end="6">
                            <c:if test="${availableDays[week][day] == false}">
                                <c:if test="${dateArray[week][day] != 'NA'}">
                                    <td class="unavailable">${dateArray[week][day]}</td>
                                </c:if>
                                <c:if test="${dateArray[week][day] == 'NA'}">
                                    <td></td>
                                </c:if>
                            </c:if>
                            <c:if test="${availableDays[week][day] == true}">
                                <td class="available" onclick="showDaySchedule(${week}, ${day}, ${count})">${dateArray[week][day]}</td>
                                <c:set var="count" value="${count + 1}" />
                            </c:if>
                        </c:forEach>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div class="day-schedule" id="daySchedulePanel" style="display:none;">
            <h3>Available Hours</h3>
            <div id="availableHours"></div>
            <label for="weeks">Number of weeks to book:</label>
            <select id="weeks" name="weeks" class="form-control bg-dark text-white w-25">
                <option value="1">1</option>
                <option value="2">2</option>
                <option value="3">3</option>
                <option value="4">4</option>
            </select>
            <input type="hidden" id="confirmDay" name="day" />
            <input type="hidden" id="confirmHour" name="hour" />
            <button type="button" class="confirm-btn btn btn-primary text-white mt-3" onclick="confirmBooking()">Confirm Booking</button>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function showDaySchedule(week, day, count) {
            var weeklySchedules = JSON.parse('${weeklySchedulesJson}');
            var arrSchedule = weeklySchedules[week].arrSchedule;
            var hours = arrSchedule[day];
            var availableHoursDiv = document.getElementById("availableHours");
            availableHoursDiv.innerHTML = "";

            for (let hour = 0; hour < 24; hour++) {
                if (hours[hour] == 'Open') {
                    var hourButton = document.createElement("button");
                    hourButton.innerText = hour + ":00";
                    hourButton.className = "btn btn-dark text-white";
                    hourButton.onclick = function() {
                        document.getElementById("confirmDay").value = day;
                        document.getElementById("confirmHour").value = hour;
                        document.getElementById("weeks").style.display = "block";
                        document.querySelector(".confirm-btn").style.display = "block";
                    };
                    availableHoursDiv.appendChild(hourButton);
                }
            }

            var weekcount = Math.ceil(count / 7);
            if (weekcount > 1) {
                var selectElement = document.getElementById("weeks");
                var numToRemove = weekcount - 1;
                for (var i = 0; i < numToRemove; i++) {
                    selectElement.remove(4 - i - 1);
                }
            }

            document.getElementById("daySchedulePanel").style.display = "block";
        }

        function confirmBooking() {
            var day = document.getElementById("confirmDay").value;
            var hour = document.getElementById("confirmHour").value;
            var weeks = document.getElementById("weeks").value;

            var form = document.createElement("form");
            form.method = "POST";
            form.action = "confirmBookingServlet";

            var tutorIdInput = document.createElement("input");
            tutorIdInput.type = "hidden";
            tutorIdInput.name = "tutorId";
            tutorIdInput.value = "${tutorId}";
            form.appendChild(tutorIdInput);

            var dayInput = document.createElement("input");
            dayInput.type = "hidden";
            dayInput.name = "day";
            dayInput.value = day;
            form.appendChild(dayInput);

            var hourInput = document.createElement("input");
            hourInput.type = "hidden";
            hourInput.name = "hour";
            hourInput.value = hour;
            form.appendChild(hourInput);

            var weeksInput = document.createElement("input");
            weeksInput.type = "hidden";
            weeksInput.name = "weeks";
            weeksInput.value = weeks;
            form.appendChild(weeksInput);

            document.body.appendChild(form);
            form.submit();
        }
    </script>
</body>
</html>
