package com.algonquin.ToiTalk.DAO;

import com.algonquin.ToiTalk.model.Language;
import com.algonquin.ToiTalk.model.Schedule;
import com.algonquin.ToiTalk.model.Student;
import com.algonquin.ToiTalk.model.Tutor;
import com.algonquin.ToiTalk.model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


	// Entry point for the application, so it loads persistent objects
public class UserDAO {
	private Connection connection;
	
	public UserDAO(Connection connection) {
		this.connection = connection;
	}
	
    public User validateUser(String email, String password) {

    		//join both student and tutor onto user
            String sql = "SELECT * FROM users LEFT JOIN tutors"
            		   + "ON users.user_id = tutors.user_id"
            		   + "LEFT JOIN students ON students.user_id = user.user_id"
            		   + "WHERE email = ? AND password = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
            	if(rs.getString("user_type")=="student") {
            		//TODO create logic for selecting student/tutor
            		//TODO create logic for pulling info from tutors table
                return new Student(rs.getString("username"), password, email, rs.getString("user_type")
                		, rs.getInt("user_id"), rs.getTimestamp("create_time"), rs.getInt("student_id"));
            	} else {
            		//retrieve tutorID as variable
            		int tutorID = rs.getInt("tutor_id");
            		
                	//assign blank value to 'bio' if null
                	String bio = rs.getString("bio");
                	bio = (bio != null) ? bio : "";
                	
                	//load all languages
                	LanguageDAO langDAO = new LanguageDAO(connection);
                	List<Language> matchList = langDAO.matchLang(tutorID);
                	
                	//create tutorSchedule
                	Schedule tutorSchedule;
                	
                	//load Schedule
            		ScheduleDAO scheduleDAO = new ScheduleDAO(connection);
                    	tutorSchedule = scheduleDAO.loadSchedule(tutorID);
                	
                    //construct object	
                	return new Tutor(rs.getString("username"), password, email, rs.getString("user_type"), rs.getInt("user_id"), rs.getTimestamp("create_time")
                					 , tutorID, matchList, bio, rs.getFloat("rating"), rs.getInt("years"), tutorSchedule);
            	} 
            }
        } catch (SQLException e) {
        	e.printStackTrace();
    		System.out.println("Could not load from SQL: USER DATA");
        }
        return null;
    }
    
    public String getTutorNameByID(int ID) {
    	String tutorName = null;
		String sqlTutor = "SELECT username FROM users LEFT JOIN tutor"
				+ "ON users.user_id = tutors.user_id"
				+ "WHERE tutor_id = ?";
		try {
			PreparedStatement statement = connection.prepareStatement(sqlTutor);
			statement.setInt(1, ID);
			ResultSet rs = statement.executeQuery();
			rs.next();
			tutorName = rs.getString("username");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tutorName;
    }
    
    public String getStudentNameByID(int ID) {
    	String studentName = null;
		String sqlStudent = "SELECT username FROM users LEFT JOIN students"
				+ "ON users.user_id = students.user_id"
				+ "WHERE student_id = ?";
		try {
			PreparedStatement statement = connection.prepareStatement(sqlStudent);
			statement.setInt(1, ID);
			ResultSet rs = statement.executeQuery();
			rs.next();
			studentName = rs.getString("username");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return studentName;
    }
}