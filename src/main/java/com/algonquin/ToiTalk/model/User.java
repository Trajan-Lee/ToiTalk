package com.algonquin.ToiTalk.model;
import java.sql.Timestamp;

public class User {
    private String username;
    private String password;
    private String email;
    private String type;
    private Timestamp createtime;
    private int userID;
    
    // Constructor
    public User(String username, String password, String email, String type, int ID, Timestamp create_time) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.type = type;
        this.userID = ID;
        this.createtime = create_time;
        
    }



	// Getters and Setters
    public int getUserID() {
		return userID;
	}
    
	public void setUserID(int userID) {
		this.userID = userID;
	}
	
    public String getUsername() {
        return username;
    }
    
	public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Timestamp getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Timestamp create_time) {
		this.createtime = create_time;
	}
}