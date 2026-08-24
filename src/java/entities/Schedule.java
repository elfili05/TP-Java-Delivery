package java.entities;

import java.time.*;

public class Schedule {
	
	private int schedule_number;
	private int restaurant_id;
	private LocalTime start_time;
	private LocalTime end_time;
	private DayOfWeek day_of_week;
	int getSchedule_number() {
		return schedule_number;
	}
	void setSchedule_number(int schedule_number) {
		this.schedule_number = schedule_number;
	}
	int getRestaurant_id() {
		return restaurant_id;
	}
	void setRestaurant_id(int restaurant_id) {
		this.restaurant_id = restaurant_id;
	}
	LocalTime getStart_time() {
		return start_time;
	}
	void setStart_time(LocalTime start_time) {
		this.start_time = start_time;
	}
	LocalTime getEnd_time() {
		return end_time;
	}
	void setEnd_time(LocalTime end_time) {
		this.end_time = end_time;
	}
	String getDay_of_week() {
		return day_of_week.toString().toLowerCase();
	}
	void setDay_of_week(DayOfWeek day_of_week) {
		this.day_of_week = day_of_week;
	}
	
	
	
}

