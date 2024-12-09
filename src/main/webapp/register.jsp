
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Create New User</title>
    <style>
        .error-message {
            color: red;
            font-weight: bold;
            margin-top: 20px;
        }
        .language-box {
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 5px 10px;
            margin: 5px;
            border: 1px solid #ddd;
            border-radius: 4px;
            background-color: #f9f9f9;
            position: relative;
            max-width: 75px;
        }
        .language-box .text-container {
            flex-grow: 1;
        }
        .remove-btn {
            background-color: transparent;
            border: none;
            color: red;
            font-weight: bold;
            cursor: pointer;
            position: absolute;
            top: 0;
            right: 0;
            align-self: center;
        }
        .add-language-btn {
            margin-top: 10px;
            padding: 5px 10px;
            background-color: #4CAF50;
            color: white;
            border: none;
            cursor: pointer;
            border-radius: 4px;
        }
        .language-dropdown-box {
            margin-top: 10px;
            padding: 10px;
            border: 1px solid #ddd;
            background-color: #f0f0f0;
            border-radius: 4px;
        }
        .confirm-btn, .cancel-btn {
            font-weight: bold;
            cursor: pointer;
            margin-left: 5px;
        }
        .confirm-btn {
            color: green;
        }
        .cancel-btn {
            color: red;
        }
        #messageBox {
            display: none;
            padding: 10px;
            margin: 10px 0;
            border-radius: 5px;
        }
        #messageBox {
            color: #fff;
            text-align: center;
        }
        .error {
            background-color: #f44336;
        }
        .success {
            background-color: #4CAF50;
        }
    </style>
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
</head>
<body>
    <h1>Create New User</h1>

    <form action="createUserServlet" method="post">
        <label for="username">Username:</label>
        <input type="text" id="username" name="username" required /><br/><br/>

        <label for="email">Email:</label>
        <input type="email" id="email" name="email" required /><br/><br/>

        <label for="password">Password:</label>
        <input type="password" id="password" name="password" required /><br/><br/>

        <label for="userType">User Type:</label>
        <input type="checkbox" id="userType" name="userType" value="tutor" onclick="toggleUserType()" /> Tutor<br/><br/>

        <div id="tutorFields" style="display: none;">
            <label for="bio">Bio:</label>
            <textarea id="bio" name="bio" rows="4" cols="50"></textarea><br/><br/>

            <div id="messageBox"></div>

            <label>Languages:</label>
            <div id="languagesContainer">
                <button type="button" class="add-language-btn" onclick="toggleAddLanguageBox()">Add</button>
            </div>

            <div id="languageDropdownBox" class="language-dropdown-box" style="display: none;">
                <label for="newLanguage">Select Language:</label>
                <select id="newLanguage" name="newLanguage">
                    <c:forEach var="allLang" items="${allLang}">
                        <option value="${allLang}">${allLang}</option>
                    </c:forEach>
                </select>
                <button type="button" class="confirm-btn" onclick="confirmAddLanguage()">✔</button>
                <button type="button" class="cancel-btn" onclick="toggleAddLanguageBox()">X</button>
            </div>

            <label for="expYears">Years of Experience:</label>
            <input type="number" id="expYears" name="expYears" /><br/><br/>
        </div>
        
		<c:if test="${not empty errorMessage}">
		    <div class="error-message">${errorMessage}</div>
		</c:if>
        

        <button type="submit">Create User</button>
    </form>
</body>
</html>
