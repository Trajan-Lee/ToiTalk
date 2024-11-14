package com.algonquin.ToiTalk.DAO;

import com.algonquin.ToiTalk.model.Language;
import com.algonquin.ToiTalk.model.Schedule;
import com.algonquin.ToiTalk.model.Student;
import com.algonquin.ToiTalk.model.Tutor;
import com.algonquin.ToiTalk.model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;


	// Entry point for the application, so it loads persistent objects
public class UserDAO {
	private Connection connection;
	
	public UserDAO(Connection connection) {
		this.connection = connection;
	}
	
    public User validateUser(String email, String password) {
    	User user;
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
            	user = loadUser(rs);
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
    
 // Method to create a new student
    public Student createStudent(Student student) {
        String sqlUser = "INSERT INTO users (username, email, password, user_type) VALUES (?, ?, ?, 'student')";
        String sqlStudent = "INSERT INTO students (user_id) VALUES (?)";

        try (PreparedStatement userStmt = connection.prepareStatement(sqlUser, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement studentStmt = connection.prepareStatement(sqlStudent)) {

            // Insert into users table
            userStmt.setString(1, student.getUsername());
            userStmt.setString(2, student.getEmail());
            userStmt.setString(3, student.getPassword());
            userStmt.executeUpdate();

            // Retrieve the generated user ID
            ResultSet rs = userStmt.getGeneratedKeys();
            if (rs.next()) {
                int userId = rs.getInt(1);
                student.setUserID(userId); 

                // Insert into students table using the user_id
                studentStmt.setInt(1, userId);
                studentStmt.executeUpdate();
                try (ResultSet generatedKeys = studentStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        // Get the auto-incremented feedback_id
                        student.setStudentID(generatedKeys.getInt(1));
                    } else {
                        System.err.println("No feedback ID obtained.");
                    }
                }
                return student;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error creating student: " + e.getMessage());
        }
        return student;
    }

    // Method to create a new tutor
    public Tutor createTutor(Tutor tutor) {
        String sqlUser = "INSERT INTO users (username, email, password, user_type) VALUES (?, ?, ?, 'tutor')";
        String sqlTutor = "INSERT INTO tutors (user_id, bio, rating, years) VALUES (?, ?, ?, ?)";

        try (PreparedStatement userStmt = connection.prepareStatement(sqlUser, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement tutorStmt = connection.prepareStatement(sqlTutor, Statement.RETURN_GENERATED_KEYS)) {

            // Insert into users table
            userStmt.setString(1, tutor.getUsername());
            userStmt.setString(2, tutor.getEmail());
            userStmt.setString(3, tutor.getPassword());
            userStmt.executeUpdate();

            // Retrieve the generated user ID
            ResultSet rs = userStmt.getGeneratedKeys();
            if (rs.next()) {
                int userId = rs.getInt(1);
                tutor.setUserID(userId);

                // Insert into tutors table using the user_id and Tutor-specific details
                tutorStmt.setInt(1, userId);
                tutorStmt.setString(2, tutor.getBio());
                tutorStmt.setFloat(3, tutor.getRating());
                tutorStmt.setInt(4, tutor.getExpYears());
                
                // Handle the languages (assuming LanguageDAO's updateTutorLanguage method updates the tutor's languages)
                LanguageDAO languageDAO = new LanguageDAO(connection);
                boolean languageSuccess = languageDAO.updateTutorLanguage(tutor);

                if (languageSuccess) {
                    // Insert tutor info into the tutors table
                    tutorStmt.executeUpdate();
                    // If tutor ID is generated, set it to the tutor object
                    ResultSet generatedKeys = tutorStmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        tutor.setTutorID(generatedKeys.getInt(1));
                    } else {
                        System.err.println("No tutor ID obtained.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error creating tutor: " + e.getMessage());
        } 
        return tutor;
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
            //delete from tutor_languages
			Class.forName("com.mysql.cj.jdbc.Driver");
			LanguageDAO languageDAO = new LanguageDAO(connection);
			boolean languageSuccess = languageDAO.deleteAllTutorLang(userId);
			if (languageSuccess == true) {
				return true;
			} else {
				return false;
			}

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error deleting tutor: " + e.getMessage());
            return false;
        } catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
        }
    }
    
    public Tutor getTutorObjByID(int ID) {
    	Tutor tutor = null;
    	String sqlTutor = "SELECT username FROM users "
    			+ "LEFT JOIN tutors ON users.user_id = tutors.user_id "
    			+ "WHERE tutor_id = ?";
    	
		try (PreparedStatement statement = connection.prepareStatement(sqlTutor)){
			statement.setInt(1, ID);
			ResultSet rs = statement.executeQuery();
			rs.next();
	        if (rs.next()) {  // Check if a result exists
	            tutor = (Tutor) loadUser(rs); // Cast loadUser result to Tutor
	        } else {
	            System.out.println("No tutor found with the specified ID.");
	        }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tutor;
    }
    
    public Student getStudentObjByID(int ID) {
    	Student student = null;
    	String sqlStudent = "SELECT username FROM users "
    			+ "LEFT JOIN students ON users.user_id = students.user_id "
    			+ "WHERE student_id = ?";

		try (PreparedStatement statement = connection.prepareStatement(sqlStudent)){
			statement.setInt(1, ID);
			ResultSet rs = statement.executeQuery();
			rs.next();
	        if (rs.next()) {  // Check if a result exists
	            student = (Student) loadUser(rs); // Cast loadUser result to Tutor
	        } else {
	            System.out.println("No student found with the specified ID.");
	        }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return student;
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
			e.printStackTrace();
		}
		return studentName;
    }
    
    public List<User> searchTutorByNameOrLang(String input, Boolean langYes) {
    	List<User> returnUser;
    	
    	if (langYes == true) {
    		returnUser = subSearchTutorByLang(input);        	
        } else {
        	returnUser = subSearchTutorByName(input);
        }
    	return returnUser;
    }

    public List<User> subSearchTutorByName(String name){
    	
    	String sql = "SELECT * FROM tutors "
        		+ "LEFT JOIN users "
        		+ "ON tutors.user_id = tutors.user_id "
        		+ "WHERE username LIKE ?";
    	
    	List<User> users = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(2, "%" + name + "%");
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
    
    public List<User> subSearchTutorByLang(String lang){
    	
    	String sql = "SELECT * FROM tutor_languages "
        		+ "LEFT JOIN tutors "
        		+ "ON tutors.tutor_id = tutors.tutor_languages.tutor_id "
        		+ "LEFT JOIN languages "
        		+ "ON languages.language_id = tutor_languages.language_id "
        		+ "WHERE language_name LIKE ?";
    	
    	List<User> users = new ArrayList<>();
    	
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(2, "%" + lang + "%");
            ResultSet rs = statement.executeQuery();
            User user;
            while (rs.next()) {
            	user = loadUser(rs);
            	users.add(user);
            }
        } catch (SQLException e) {
        	e.printStackTrace();
            System.err.println("Error searching by language: " + e.getMessage());
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
            tutorStmt.setInt(3, tutor.getExpYears());
            tutorStmt.setInt(4, tutor.getTutorID());
			Class.forName("com.mysql.cj.jdbc.Driver");
			LanguageDAO languageDAO = new LanguageDAO(connection);
			boolean languageSuccess = languageDAO.updateTutorLanguage(tutor);
			if (languageSuccess == true) {
                tutorStmt.executeUpdate();
                return true;
			} else {
				return false;
			}
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error updating tutor: " + e.getMessage());
            return false;
        } catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
	    }
	}
}
    
