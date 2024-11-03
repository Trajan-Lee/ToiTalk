package com.algonquin.ToiTalk.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.algonquin.ToiTalk.model.Language;

public class LanguageDAO {
	private Connection connection;
	
	public LanguageDAO(Connection connection) {
		this.connection = connection;
	}
	
	//no need for code to add languages, it can easily be supported on the back end DB
	
	public List<Language> loadLang() {
		List<Language> langList = new ArrayList<>();
		
    	String sql = "SELECT * FROM languages";
    	
        try {
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				Language newLang = new Language(rs.getInt("language_ID"), rs.getString("language_name"));
				langList.add(newLang);
			}
		} catch (SQLException e) {
			e.printStackTrace();
    		System.out.println("Could not load from SQL: LANGUAGE TABLE");
		}
        
		return langList;
	}
	
	public List<Language> matchLang(int tutorID){
		List<String> tutorLang = new ArrayList<>();
		List<Language> matchLang;
		List<Language> allLang = loadLang();
		
		String sql = "SELECT * FROM tutor_languages LEFT JOIN languages"
				   + "ON tutor_languages.language_id = languages.language_id"
				   + "WHERE tutor_id = ?";
	   try {
		   PreparedStatement statement = connection.prepareStatement(sql);
		   statement.setInt(1, tutorID);
		   ResultSet rs = statement.executeQuery();
		   while (rs.next()) {
			   tutorLang.add(rs.getString("language_name"));
		   }
	   } catch (SQLException e) {
		   e.printStackTrace();
   		   System.out.println("Could not load from SQL: TUTOR_LANGUAGE TABLE");
	   }
	   
	   matchLang = allLang.stream()
			   .filter(lang -> tutorLang.contains(lang.getLangName()))
			   .collect(Collectors.toList());
		return matchLang;
	}
}
