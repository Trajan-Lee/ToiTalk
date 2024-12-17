
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Profile</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="supplemental/styles.css">
</head>
<body>
<jsp:include page="supplemental/toolbar.jsp" />
    <div class="main-card card-large">
        <h2>
            <span class="brand-title">Toi</span><span class="brand-subtitle">Talk</span>
        </h2>
        <h4 class="text-center mb-4">Edit Profile</h4>

        <form action="submitProfileServlet" method="post">
            <div class="mb-3">
                <label for="username" class="form-label">Username</label>
                <input type="text" class="form-control bg-dark text-white" id="username" name="username" value="${user.getUsername()}" required>
            </div>

            <div class="mb-3">
                <label for="email" class="form-label">Email</label>
                <input type="email" class="form-control bg-dark text-white" id="email" name="email" value="${user.getEmail()}" required>
            </div>

            <c:choose>
                <c:when test="${user.getType() == 'student'}">
                    <p>You are editing a Student profile.</p>
                </c:when>

                <c:when test="${user.getType() == 'tutor'}">
                    <p>You are editing a Tutor profile.</p>

                    <div class="mb-3">
                        <label for="bio" class="form-label">Bio</label>
                        <textarea class="form-control bg-dark text-white" id="bio" name="bio" rows="4">${user.getBio()}</textarea>
                    </div>

                    <div id="messageBox"></div>

                    <div class="mb-3">
                        <label for="languages" class="form-label">Languages</label>
                        <div id="languagesContainer">
                            <c:forEach var="lang" items="${user.getLanguages()}">
                                <div id="lang-${lang.getLangName()}" class="language-box">
                                    <div class="text-container">
                                        ${lang.getLangName()}
                                        <input type="hidden" name="languages" value="${lang.getLangName()}">
                                    </div>
                                    <button type="button" class="remove-btn" onclick="removeLanguage('${lang.getLangName()}')">X</button>
                                </div>
                            </c:forEach>
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
                        <input type="number" class="form-control bg-dark text-white" id="expYears" name="expYears" value="${user.getExpYears()}">
                    </div>
                </c:when>
            </c:choose>

            <div class="d-grid">
                <button type="submit" class="btn btn-primary text-white">Save Changes</button>
            </div>
        </form>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
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

            document.getElementById('languagesContainer').insertBefore(newLangDiv, document.querySelector('.btn-success'));
            toggleAddLanguageBox();
        }
    </script>
</body>
</html>
