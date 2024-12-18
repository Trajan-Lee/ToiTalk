package com.algonquin.ToiTalk.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.algonquin.ToiTalk.model.Schedule;

public class ScheduleDAO {
	Connection connection;
	
	public ScheduleDAO(Connection connection) {
		this.connection = connection;
	}
	
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
    
    private static final Map<Integer, String> intToDayMap;
    static {
        intToDayMap = new HashMap<>();
        intToDayMap.put(0, "Monday");
        intToDayMap.put(1, "Tuesday");
        intToDayMap.put(2, "Wednesday");
        intToDayMap.put(3, "Thursday");
        intToDayMap.put(4, "Friday");
        intToDayMap.put(5, "Saturday");
        intToDayMap.put(6, "Sunday");
    }
	private Integer DayConverter(String Day) {
		return dayToIntMap.get(Day);
	}
	
	public List<Schedule> getWeeklySchedules(Schedule schedule, int weeks) {
		// ensure only 1-4 weeks are requested
        if (weeks < 1 || weeks > 6) {
            throw new IllegalArgumentException("Weeks must be between 1 and 4");
        }

        List<Schedule> weeklySchedules = new ArrayList<>();
        int tutorID = schedule.getTutorID();
        String[][] baseSchedule = schedule.getArrSchedule();
        
        //create deep copy of array
        for (int week = 0; week < weeks; week++) {
            String[][] weekSchedule = new String[7][24];
            for (int day = 0; day < 7; day++) {
                System.arraycopy(baseSchedule[day], 0, weekSchedule[day], 0, 24);
            }
            
            // retrieve bookings for the week
            String sql = "SELECT * FROM bookings WHERE tutor_id = ? AND WEEK(date) = WEEK(CURDATE()) + ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, tutorID);
                statement.setInt(2, week);
                ResultSet rs = statement.executeQuery();
                
                // remove bookings from schedule
                while (rs.next()) {
                    int bookingDay = rs.getTimestamp("date").toLocalDateTime().getDayOfWeek().getValue() - 1;
                    int bookingHour = rs.getTimestamp("date").toLocalDateTime().getHour();
                    weekSchedule[bookingDay][bookingHour] = null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Could not load from SQL: BOOKING DATA");
            }

            weeklySchedules.add(new Schedule(tutorID, weekSchedule));
        }

        return weeklySchedules;
    }
	
	public Schedule loadSchedule(int tutorID) {
		String schedule[][] = new String[7][24];
		

		String sql = "SELECT * FROM tutor_schedule "
				+ "LEFT JOIN time_slots "
				+ "ON tutor_schedule.slot_id = time_slots.slot_id "
				+ "WHERE tutor_schedule.tutor_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)){
	        statement.setInt(1, tutorID);
	        ResultSet rs = statement.executeQuery();
	        int iDay;
	        int iHour;
	        String sStatus;
			while (rs.next()) {
				iDay = DayConverter(rs.getString("day"));
				iHour = rs.getInt("hour");
				sStatus = "Open";
				schedule[iDay][iHour] = sStatus;
			}
		} catch (SQLException e) {
    		e.printStackTrace();
    		System.out.println("Could not load from SQL: SCHEDULE DATA");
		}
		
		return new Schedule(tutorID, schedule);
	}
	
	//Will be run every time an update is performed. Essentially, wipes the schedule and then rebuilds it in DB
	public boolean deleteSchedule(int tutorID) {
		
		String sql = "DELETE FROM tutor_schedule "
				+ "WHERE tutor_id = ?";
		
        try (PreparedStatement statement = connection.prepareStatement(sql)){
        	statement.setInt(1, tutorID);
        	statement.executeUpdate();
        	return true;
        } catch (SQLException e) {
        	e.printStackTrace();
        	System.out.println("Error deleting schedule" + e.getMessage());
        	return false;
        }
	}
	
	public boolean addSchedule(Schedule schedule) {
	    String[][] inSchedule = schedule.getArrSchedule();
	    int tutorID = schedule.getTutorID();

	    String sql = "INSERT INTO tutor_schedule (tutor_id, slot_id) VALUES (?, ?)";

	    try (PreparedStatement statement = connection.prepareStatement(sql)) {
	        statement.setInt(1, tutorID);
	        int slotID;

	        for (int day = 0; day < 7; day++) {
	            for (int hour = 0; hour < 24; hour++) {
	                if (inSchedule[day][hour] != null) {
	                    slotID = day * 24 + hour + 1;
	                    statement.setInt(2, slotID);
	                    statement.addBatch(); // Add to batch instead of executing immediately
	                }
	            }
	        }
	        if (deleteSchedule(tutorID)) {
		        statement.executeBatch(); // Execute all at once
		        return true;
	        } else {
	        	System.out.println("Failed to delete old entries");
	        	return false;
	        }

	    } catch (SQLException e) {
	    	e.printStackTrace();
	        System.out.println("Failed to add schedule: " + e.getMessage());
	        return false;
	    }
	}
	
	public int generateSlotID(int day, int hour) {
		int slotID =  day * 24 + hour + 1;
		return slotID;
	}
	
	/*  Depreciated for now. Status Not implemented in DB
	 * 
	 * 
	public boolean changeScheduleStatus(int tutorID, int day, int hour, String status) {
		
		String sql = "UPDATE tutor_schedule"
				+ "SET status = ?"
				+ "WHERE tutor_id = ?"
				+ "AND slot_id = ?";
		
		int slotID = generateSlotID(day, hour);
		
	    try (PreparedStatement statement = connection.prepareStatement(sql)) {
	    	statement.setString(1, status);
	    	statement.setInt(2, tutorID);
	    	statement.setInt(3, slotID);
	    	return true;
	    	
	    } catch (SQLException e) {
	    	e.printStackTrace();
	    	return false;
	    }
	}
	
	public boolean changeScheduleStatus(int tutorScheduleID, String status) {
		String sql = "UPDATE tutor_schedule"
				+ "SET status = ?"
				+ "WHERE tutor_schedule_id = ?";
		
	    try (PreparedStatement statement = connection.prepareStatement(sql)) {
	    	statement.setString(1, status);
	    	statement.setInt(2, tutorScheduleID);
	    	return true;
	    	
	    } catch (SQLException e) {
	    	e.printStackTrace();
	    	return false;
	    }
	}
	*/
}
