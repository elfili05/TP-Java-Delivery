package main.java.logic;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.LinkedList;

import main.java.data.RestaurantRepository;
import main.java.entities.Restaurant;
import main.java.entities.Schedule;

public class RestaurantCRUD {

	private RestaurantRepository rr;

	public RestaurantCRUD() {
		rr = new RestaurantRepository();
	}
	
	public LinkedList<Restaurant> getAvailable() throws SQLException {
		return rr.getAll();
	}
	
	
	public boolean isAvailable(Restaurant restaurantToCheck) throws SQLException {
		return rr.isAvailable(restaurantToCheck);
	}
	
	
	public Restaurant getRestaurant(Restaurant restaurantToFind) throws SQLException {
		return rr.getOne(restaurantToFind);
	}
	
	
	public Boolean addRestaurant(Restaurant restaurantToAdd) throws SQLException {
		return rr.addRestaurant(restaurantToAdd);
	}

	// usados por el panel admin para editar/eliminar restaurantes.
	public Boolean updateRestaurant(Restaurant restaurantToUpdate) throws SQLException {
		return rr.updateRestaurant(restaurantToUpdate);
	}

	public Boolean deleteRestaurant(int restaurantId) throws SQLException {
		return rr.deleteRestaurant(restaurantId);
	}

	// usados por el panel admin para gestionar los horarios de un restaurante.
	public LinkedList<Schedule> getSchedules(int restaurantId) throws SQLException {
		return rr.getSchedules(restaurantId);
	}

	// asigna el próximo schedule_number libre para ese restaurante antes de insertar.
	public Boolean addSchedule(Restaurant restaurant, Schedule schedule) throws SQLException {
		int nextNumber = 1;
		for (Schedule existing : rr.getSchedules(restaurant.getRestaurant_id())) {
			if (existing.getSchedule_number() >= nextNumber) {
				nextNumber = existing.getSchedule_number() + 1;
			}
		}
		schedule.setSchedule_number(nextNumber);
		return rr.addSchedule(restaurant, schedule);
	}

	public Boolean updateSchedule(Schedule schedule) throws SQLException {
		return rr.updateSchedule(schedule);
	}

	public Boolean deleteSchedule(int restaurantId, int scheduleNumber) throws SQLException {
		return rr.deleteSchedule(restaurantId, scheduleNumber);
	}

	// un restaurante puede tener varios horarios el mismo día (ej. abre a la mañana y a la tarde),
	// pero no dos que se superpongan; excludeScheduleNumber se usa al editar para no compararse contra sí mismo.
	public boolean hasScheduleOverlap(int restaurantId, Schedule candidate, Integer excludeScheduleNumber) throws SQLException {
		for (Schedule existing : rr.getSchedules(restaurantId)) {
			if (excludeScheduleNumber != null && existing.getSchedule_number() == excludeScheduleNumber) {
				continue;
			}
			if (!existing.getDay_of_week().equalsIgnoreCase(candidate.getDay_of_week())) {
				continue;
			}
			if (rangesOverlap(existing, candidate)) {
				return true;
			}
		}
		return false;
	}

	private boolean rangesOverlap(Schedule a, Schedule b) {
		LocalTime aStart = a.getStart_time().toLocalTime();
		LocalTime aEnd = normalizeEndOfDay(aStart, a.getEnd_time().toLocalTime());
		LocalTime bStart = b.getStart_time().toLocalTime();
		LocalTime bEnd = normalizeEndOfDay(bStart, b.getEnd_time().toLocalTime());
		return aStart.isBefore(bEnd) && bStart.isBefore(aEnd);
	}

	// un cierre cargado como "00:00" significa "hasta la medianoche", no "a la medianoche ya cerrado".
	private LocalTime normalizeEndOfDay(LocalTime start, LocalTime end) {
		return end.equals(LocalTime.MIDNIGHT) ? LocalTime.MAX : end;
	}
}
