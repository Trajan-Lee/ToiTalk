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
    		String sql = "SELECT * FROM users LEFT JOIN tutors "
    			   	   + "ON users.user_id = tutors.user_id "
    			   	   + "LEFT JOIN students ON users.user_id = students.user_id "
    			   	   + "WHERE email = ? AND password = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
            	loadUser(rs);
            }


        } catch (SQLException e) {
        	e.printStackTrace();
    		System.out.println("Could not load from SQL: USER DATA");
        }
        return null;
    }
    
    public User loadUser(ResultSet rs) throws SQLException {
    	User user = null;
				if(rs.getString("user_type")=="student") {
					//TODO create logic for selecting student/tutor
					//TODO create logic for pulling info from tutors table
				user = new Student(rs.getString("username"), rs.getString("password"), rs.getString("email"), rs.getString("user_type")
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
					user =  new Tutor(rs.getString("username"), rs.getString("password"), rs.getString("email"), rs.getString("user_type"), rs.getInt("user_id"), rs.getTimestamp("create_time")
									 , tutorID, matchList, bio, rs.getFloat("rating"), rs.getInt("years"), tutorSchedule);
				}
        	return user;
        }
    
    public String getTutorNameByID(int ID) {
    	String tutorName = null;
    	String sqlTutor = "SELECT username FROM users "
    			+ "LEFT JOIN tutors ON users.user_id = tutors.user_id "
    			+ "WHERE tutor_id = ?";

		try (PreparedStatement statement = connection.prepareStatement(sqlTutor)){
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
    	String sqlStudent = "SELECT username FROM users "
    			+ "LEFT JOIN students ON users.user_id = students.user_id "
    			+ "WHERE student_id = ?";

		try (PreparedStatement statement = connection.prepareStatement(sqlStudent)){
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
    
    public List<User> searchByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username LIKE ?";
        List<User> users = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, "%" + username + "%");
            ResultSet rs = statement.executeQuery();
            User user;
            while (rs.next()) {
            	user = loadUser(rs);
            	users.add(user);
            }
        } catch (SQLException e) {
        	e.printStackTrace();
            System.err.println("Error searching by username: " + e.getMessage());
        }

        return users;
    }
    
    // Update a Student's information
    public boolean updateStudent(Student student) {
        String sqlUser = "UPDATE users SET username = ?, email = ?, password = ? WHERE user_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sqlUser)) {

            // Update in users table
            statement.setString(1, student.getUsername());
            statement.setString(2, student.getEmail());
            statement.setString(3, student.getPassword());
            statement.setInt(4, student.getUserID());
            statement.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error updating student: " + e.getMessage());
            return false;
        }
    }
    
    public boolean updateTutor(Tutor tutor) {
        String sqlUser = "UPDATE users SET username = ?, email = ?, password = ? WHERE user_id = ?";
        String sqlTutor = "UPDATE tutors SET bio = ?, rating = ?, years = ? WHERE user_id = ?";

        try (PreparedStatement userStmt = connection.prepareStatement(sqlUser);
             PreparedStatement tutorStmt = connection.prepareStatement(sqlTutor)) {

            // Update in users table
            userStmt.setString(1, tutor.getUsername());
            userStmt.setString(2, tutor.getEmail());
            userStmt.setString(3, tutor.getPassword());
            userStmt.setInt(4, tutor.getTutorID());
            userStmt.executeUpdate();

            // Update in tutors table
            tutorStmt.setString(1, tutor.getBio());
            tutorStmt.setFloat(2, tutor.getRating());
            tutorStmt.setInt(3, tutor.getExpyears());
            tutorStmt.setInt(4, tutor.getTutorID());
            tutorStmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error updating tutor: " + e.getMessage());
            return false;
        }   
    }
    
    public boolean deleteStudent(int userId) {
        String sqlStudent = "DELETE FROM students WHERE user_id = ?";
        String sqlUser = "DELETE FROM users WHERE user_id = ?";

        try (PreparedStatement studentStmt = connection.prepareStatement(sqlStudent);
             PreparedStatement userStmt = connection.prepareStatement(sqlUser)) {

            // Delete from students table
            studentStmt.setInt(1, userId);
            studentStmt.executeUpdate();

            // Delete from users table
            userStmt.setInt(1, userId);
            userStmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error deleting student: " + e.getMessage());
            return false;
        }
    }
    
    public boolean deleteTutor(int userId) {
        String sqlTutor = "DELETE FROM tutors WHERE user_id = ?";
        String sqlUser = "DELETE FROM users WHERE user_id = ?";

        try (PreparedStatement tutorStmt = connection.prepareStatement(sqlTutor);
             PreparedStatement userStmt = connection.prepareStatement(sqlUser)) {

            // Delete from tutors table
            tutorStmt.setInt(1, userId);
            tutorStmt.executeUpdate();

            // Delete from users table
            userStmt.setInt(1, userId);
            userStmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error deleting tutor: " + e.getMessage());
            return false;
        }
    }
}
