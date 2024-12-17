<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!-- Script for opening and closing the sidenav -->
<script>        
    function openNav() {
        document.getElementById("Sidenav").style.width = "250px";
    }

    function closeNav() {
        document.getElementById("Sidenav").style.width = "0";
    }
</script>

    <!-- Top bar -->
    <div class="top-bar">
        <h2><a href='dashboard.jsp'><span class="brand-title">Toi</span><span class="brand-subtitle">Talk</span></a></h2>
        <div class="hamburger-menu" onclick="openNav()">&#9776;</div>
    </div>

    <!-- Right-side navigation panel -->
    <div id="Sidenav" class="sidenav">
        <a href="javascript:void(0)" class="closebtn" onclick="closeNav()">&times;</a>
        <button class="btn btn-primary text-white" onclick="location.href='dashboard.jsp'">Dashboard</button>
        <button class="btn btn-primary text-white" onclick="location.href='loadProfileServlet'">View Profile</button>
        <button class="btn btn-primary text-white" onclick="location.href='viewBookingServlet'">View Booking</button>
        <c:if test="${user != null && user.type == 'student'}">
            <button class="btn btn-primary text-white" onclick="location.href='loadSearchTutorServlet'">Search Tutors</button>
        </c:if>
        <button class="btn btn-primary text-white" onclick="location.href='logoutServlet'">Log-out</button>
    </div>
