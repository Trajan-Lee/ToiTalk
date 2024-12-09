import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import com.algonquin.ToiTalk.DAO.LanguageDAO;
import com.algonquin.ToiTalk.DAO.ScheduleDAO;
import com.algonquin.ToiTalk.DAO.UserDAO;
import com.algonquin.ToiTalk.model.Language;
import com.algonquin.ToiTalk.model.Tutor;

public class AddUsers {

	public static void main(String[] args) {
	    Connection connection;

	    /*
	     * This is the main method which will add users to the database with the hashed password
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/toitalk", "user1", "password1");
			UserDAO userDAO = new UserDAO(connection);
			LanguageDAO languageDAO = new LanguageDAO(connection);
			
			String pass = "password1";
	        List<Language> lang1 = languageDAO.loadLang();
			Tutor tutor1 = new Tutor("john_tutor", pass, "john@example.com", "tutor", 0 , null, 0, lang1, "I am a tutor named John", 4.5, 5, null);
			tutor1 = userDAO.createTutor(tutor1);
			
			List<Language> lang2 = new ArrayList<>();
			lang2.add(languageDAO.getLangByName("French"));
			lang2.add(languageDAO.getLangByName("Spanish"));
			Tutor tutor2 = new Tutor("alice_tutor", pass, "alice@example.com", "tutor", 0 , null, 0, lang2, "I am a tutor named Alice", 4.5, 3, null);
			tutor2 = userDAO.createTutor(tutor2);
			
			List<Language> lang3 = new ArrayList<>();
			lang3.add(languageDAO.getLangByName("Spanish"));
			lang3.add(languageDAO.getLangByName("German"));
			Tutor tutor3 = new Tutor("bob_tutor", pass, "bob@example.com", "tutor", 0 , null, 0, lang3, "I am a tutor named Bob", 4.5, 2, null);
			tutor3 = userDAO.createTutor(tutor3);
			} catch (SQLException e) {
				e.printStackTrace();
			} 
			*/
		

	}

}
