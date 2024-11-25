<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Edit Profile</title>
    <style>
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
		    flex-grow: 1; /* Allow text container to take up available space */
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
		    background-color: #f44336; /* Red for error */
		}
		.success {
		    background-color: #4CAF50; /* Green for success */
		}
    </style>
    <script>
        function toggleAddLanguageBox() {
            const dropdownBox = document.getElementById("languageDropdownBox");
            if (dropdownBox.style.display === "none"){
            	dropdownBox.style.display = "block";
            } else {
            	dropdownBox.style.display = "none";
            }
        }
    </script>
</head>
<body>
    <h1>Edit Profile</h1>
    

    <form action="submitProfileServlet" method="post">
        <!-- Display Username and Email Fields -->
        <label for="username">Username:</label>
        <input type="text" id="username" name="username" value="${user.getUsername()}" required /><br/><br/>
        
        <label for="email">Email:</label>
        <input type="email" id="email" name="email" value="${user.getEmail()}" required /><br/><br/>
        
        <!-- Conditional Fields Based on User Type -->
        <c:choose>
            <c:when test="${user.getType() == 'student'}">
                <p>You are editing a Student profile.</p>
            </c:when>
            
            <c:when test="${user.getType() == 'tutor'}">
                <p>You are editing a Tutor profile.</p>
                
                <label for="bio">Bio:</label>
                <textarea id="bio" name="bio" rows="4" cols="50">${user.getBio()}</textarea><br/><br/>
                
		        
		        <div id="messageBox"></div>
		        
		        <label>Languages:</label>
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
                <input type="number" id="expYears" name="expYears" value="${user.getExpYears()}" /><br/><br/>
            </c:when>
        </c:choose>

        <!-- Submit Button -->
        <button type="submit">Save Changes</button>
    </form>

    <script>
	    function removeLanguage(languageToRemove) {
	        // Remove the language element from the DOM
	        const languageElement = document.getElementById("lang-"+ languageToRemove);
	        if (languageElement) {
	            languageElement.remove();
	        }
	    }


	    function confirmAddLanguage() {
	        const newLanguage = document.getElementById('newLanguage').value;

	        // Add the new language to the container
	        const newLangDiv = document.createElement('div');
			newLangDiv.className = 'language-box';
			newLangDiv.id = `lang-${newLanguage}`;
			
			// Create the text container div
			const textContainer = document.createElement('div');
			textContainer.className = 'text-container';
			
			// Add the language text to the text container
			const languageText = document.createTextNode(newLanguage);
			textContainer.appendChild(languageText);
			
			// Create the hidden input element
			const hiddenInput = document.createElement('input');
			hiddenInput.type = 'hidden';
			hiddenInput.name = 'languages';
			hiddenInput.value = newLanguage;
			textContainer.appendChild(hiddenInput);
			
			// Create the remove button
			const removeBtn = document.createElement('button');
			removeBtn.type = 'button';
			removeBtn.className = 'remove-btn';
			removeBtn.textContent = 'X';
			removeBtn.onclick = () => removeLanguage(newLanguage); // Assign the removeLanguage function
			
			// Append the text container to the newLangDiv
			newLangDiv.appendChild(textContainer);
			
			// Append the button to the newLangDiv
			newLangDiv.appendChild(removeBtn);
			
			// Insert the newLangDiv into the languagesContainer before the add-language button
			document.getElementById('languagesContainer').insertBefore(newLangDiv, document.querySelector('.add-language-btn'));


	        // Hide the add language box
	        toggleAddLanguageBox();
	    }
    </script>
</body>
</html>
