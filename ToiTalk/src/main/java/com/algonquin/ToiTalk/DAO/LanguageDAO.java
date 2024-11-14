package com.algonquin.ToiTalk.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.algonquin.ToiTalk.model.Language;
import com.algonquin.ToiTalk.model.Tutor;

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
	
	public Language getLangByName(String langName) {
		Language lang = null;
		String sql = "SELECT *"
				+ "FROM languages"
				+ "WHERE language_name = ?";
		
		try (PreparedStatement statement = connection.prepareStatement(sql)){
			statement.setString(1, langName);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				lang = new Language(rs.getInt("language_ID"), rs.getString("language_name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
    		System.out.println("Could not load from SQL: LANGUAGE TABLE");
		}
		return lang;
	}
	
	public boolean updateTutorLanguage(Tutor tutor) {
		int tutorID = tutor.getTutorID();
		boolean deleteSuccess = deleteAllTutorLang(tutorID);
		List<Language> langList = tutor.getLanguages();
		
		if (deleteSuccess == true) {
			String sql = "INSERT INTO tutor_languages (tutor_id, language_id)"
					+ "VALUES (?, ?)";
			try (PreparedStatement statement = connection.prepareStatement(sql)){
				statement.setInt(1, tutorID);
				for (Language lang : langList) {
					statement.setInt(2, lang.getLangID());
					statement.addBatch();
				}
				statement.executeBatch();
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
	    		System.out.println("Could not load from SQL: LANGUAGE TABLE");
	    		return false;
			}
		} else {
			return false;
		}
	}
	
	public boolean deleteAllTutorLang(int tutorID) {
		String sql = "DELETE *"
				+ "FROM tutor_languages"
				+ "WHERE tutor_id = ?";
		
		try (PreparedStatement statement = connection.prepareStatement(sql)){
			statement.setInt(1, tutorID);
			statement.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
    		System.out.println("Could not load from SQL: LANGUAGE TABLE");
    		return false;
		}
	}
	
	public List<Language> matchLang(int tutorID){
		//retrieves all languages that match the tutorID
		List<String> tutorLang = new ArrayList<>();
		List<Language> matchLang;
		List<Language> allLang = loadLang();
		
		String sql = "SELECT * FROM tutor_languages "
				+ "LEFT JOIN languages "
				+ "ON tutor_languages.language_id = languages.language_id "
				+ "WHERE tutor_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)){
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
	
	public List<String> listAllLang(){
		List<String> langList = new ArrayList<>();
		
    	String sql = "SELECT * FROM languages";
    	
        try {
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				langList.add(rs.getString("language_name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
    		System.out.println("Could not load from SQL: LANGUAGE TABLE");
		}
        
		return langList;
	}
	
}
