package com.algonquin.ToiTalk.controller;

import com.algonquin.ToiTalk.model.Tutor;
import com.algonquin.ToiTalk.model.User;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/removeLanguageServlet")
public class RemoveLanguageServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Parse the incoming JSON data
        JsonObject jsonResponse = new JsonObject();
        try {
            // Read the JSON data from the request
            StringBuilder jsonBuffer = new StringBuilder();
            String line;
            while ((line = request.getReader().readLine()) != null) {
                jsonBuffer.append(line);
            }

            JsonObject jsonRequest = new Gson().fromJson(jsonBuffer.toString(), JsonObject.class);
            String languageToRemove = jsonRequest.get("language").getAsString();

            // Retrieve the user from session
            User user = (User) request.getSession().getAttribute("user");

            // Check if user is a tutor and remove the language
            if (user instanceof Tutor) {
                Tutor tutor = (Tutor) user;

                boolean removed = tutor.getLanguages().removeIf(lang -> lang.getLangName().equals(languageToRemove));
                if (removed) {
                    jsonResponse.addProperty("success", true);
                } else {
                    jsonResponse.addProperty("success", false);
                    jsonResponse.addProperty("message", "Language not found in profile.");
                }
            } else {
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "User is not authorized to perform this action.");
            }

        } catch (Exception e) {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Error processing request.");
        }

        // Send JSON response back to the client
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(jsonResponse.toString());
        out.flush();
    }
}
