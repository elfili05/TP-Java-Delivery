package main.java.entities;

import java.sql.Time;

public class Schedule {
	
	private int schedule_number;
	private int restaurant_id;
	private Time start_time;
	private Time end_time;
	private String day_of_week;
	
	public int getSchedule_number() {
		return schedule_number;
	}
	public void setSchedule_number(int schedule_number) {
		this.schedule_number = schedule_number;
	}
	public int getRestaurant_id() {
		return restaurant_id;
	}
	public void setRestaurant_id(int restaurant_id) {
		this.restaurant_id = restaurant_id;
	}
	public Time getStart_time() {
		return start_time;
	}
	public void setStart_time(Time start_time) {
		this.start_time = start_time;
	}
	public Time getEnd_time() {
		return end_time;
	}
	public void setEnd_time(Time end_time) {
		this.end_time = end_time;
	}
	public String getDay_of_week() {
		return day_of_week.toLowerCase();
	}
	public void setDay_of_week(String day_of_week) {
		this.day_of_week = day_of_week;
	}
	
	
	
}

