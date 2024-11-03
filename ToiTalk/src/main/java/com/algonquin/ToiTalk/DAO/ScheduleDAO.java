package com.algonquin.ToiTalk.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.algonquin.ToiTalk.model.Schedule;

public class ScheduleDAO {
	Connection connection;
	
	//map string day to integer value for schedule array location
    private static final Map<String, Integer> dayToIntMap;

    // Static block to initialize the map once
    static {
        dayToIntMap = new HashMap<>();
        dayToIntMap.put("Monday", 0);
        dayToIntMap.put("Tuesday", 1);
        dayToIntMap.put("Wednesday", 2);
        dayToIntMap.put("Thursday", 3);
        dayToIntMap.put("Friday", 4);
        dayToIntMap.put("Saturday", 5);
        dayToIntMap.put("Sunday", 6);
    }
	private Integer DayConverter(String Day) {
		return dayToIntMap.get(Day);
	}
	
	public ScheduleDAO(Connection connection) {
		this.connection = connection;
	}
	
	public Schedule loadSchedule(int tutorID) {
		boolean schedule[][] = new boolean[7][24];
		

        String sql = "SELECT * FROM tutor_schedule LEFT JOIN time_slots"
        		   + "ON tutor_schedule.slot_id = slots.slot_id"
        		   + "WHERE tutor_slot.tutor_id = ?";
	    try {
	        PreparedStatement statement = connection.prepareStatement(sql);
	        statement.setInt(1, tutorID);
	        ResultSet rs = statement.executeQuery();
	        int iDay;
	        int iHour;
			while (rs.next()) {
				iDay = DayConverter(rs.getString("day"));
				iHour = rs.getInt("hour");
				schedule[iDay][iHour] = true;
			}
		} catch (SQLException e) {
    		e.printStackTrace();
    		System.out.println("Could not load from SQL: SCHEDULE DATA");
		}
		
		return new Schedule(tutorID, schedule);
	}
}
