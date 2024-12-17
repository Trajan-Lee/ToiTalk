
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Create New User</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Custom CSS -->
    <link rel="stylesheet" href="supplemental/styles.css">
</head>
<body>
    <div class="main-card card-large">
        <h2>
            <span class="brand-title">Toi</span><span class="brand-subtitle">Talk</span>
        </h2>
        <h4 class="text-center mb-4">Create New User</h4>

        <form action="createUserServlet" method="post">
            <div class="mb-3">
                <label for="username" class="form-label">Username</label>
                <input type="text" class="form-control bg-dark text-white" id="username" name="username" required>
            </div>

            <div class="mb-3">
                <label for="email" class="form-label">Email</label>
                <input type="email" class="form-control bg-dark text-white" id="email" name="email" required>
            </div>

            <div class="mb-3">
                <label for="password" class="form-label">Password</label>
                <input type="password" class="form-control bg-dark text-white" id="password" name="password" required>
            </div>

            <div class="form-check mb-3">
                <input type="checkbox" class="form-check-input" id="userType" name="userType" value="tutor" onclick="toggleUserType()">
                <label class="form-check-label" for="userType">Tutor</label>
            </div>

            <div id="tutorFields" style="display: none;">
                <div class="mb-3">
                    <label for="bio" class="form-label">Bio</label>
                    <textarea class="form-control bg-dark text-white" id="bio" name="bio" rows="4"></textarea>
                </div>

                <div id="messageBox"></div>

                <div class="mb-3">
                    <label for="languages" class="form-label">Languages</label>
                    <div id="languagesContainer">
                        <button type="button" class="btn btn-success" onclick="toggleAddLanguageBox()">Add</button>
                    </div>

                    <div id="languageDropdownBox" class="mt-2" style="display: none;">
                        <label for="newLanguage" class="form-label">Select Language</label>
                        <select class="form-control bg-dark text-white" id="newLanguage" name="newLanguage">
                            <c:forEach var="allLang" items="${allLang}">
                                <option value="${allLang}">${allLang}</option>
                            </c:forEach>
                        </select>
                        <button type="button" class="btn btn-success mt-2" onclick="confirmAddLanguage()">✔</button>
                        <button type="button" class="btn btn-danger mt-2" onclick="toggleAddLanguageBox()">X</button>
                    </div>
                </div>

                <div class="mb-3">
                    <label for="expYears" class="form-label">Years of Experience</label>
                    <input type="number" class="form-control bg-dark text-white" id="expYears" name="expYears">
                </div>
            </div>

            <c:if test="${not empty errorMessage}">
                <div class="error-message">${errorMessage}</div>
            </c:if>

            <div class="d-grid">
                <button type="submit" class="btn btn-primary text-white">Create User</button>
            </div>
        </form>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function toggleUserType() {
            const userType = document.getElementById("userType").checked ? "tutor" : "student";
            document.getElementById("tutorFields").style.display = userType === "tutor" ? "block" : "none";
        }

        function toggleAddLanguageBox() {
            const dropdownBox = document.getElementById("languageDropdownBox");
            dropdownBox.style.display = dropdownBox.style.display === "none" ? "block" : "none";
        }

        function removeLanguage(languageToRemove) {
            const languageElement = document.getElementById("lang-" + languageToRemove);
            if (languageElement) {
                languageElement.remove();
            }
        }

        function confirmAddLanguage() {
            const newLanguage = document.getElementById('newLanguage').value;
            const newLangDiv = document.createElement('div');
            newLangDiv.className = 'language-box';
            newLangDiv.id = `lang-${newLanguage}`;

            const textContainer = document.createElement('div');
            textContainer.className = 'text-container';
            const languageText = document.createTextNode(newLanguage);
            textContainer.appendChild(languageText);

            const hiddenInput = document.createElement('input');
            hiddenInput.type = 'hidden';
            hiddenInput.name = 'languages';
            hiddenInput.value = newLanguage;
            textContainer.appendChild(hiddenInput);

            const removeBtn = document.createElement('button');
            removeBtn.type = 'button';
            removeBtn.className = 'remove-btn';
            removeBtn.textContent = 'X';
            removeBtn.onclick = () => removeLanguage(newLanguage);

            newLangDiv.appendChild(textContainer);
            newLangDiv.appendChild(removeBtn);

            document.getElementById('languagesContainer').insertBefore(newLangDiv, document.querySelector('.add-language-btn'));
            toggleAddLanguageBox();
        }
    </script>
</body>
</html>
