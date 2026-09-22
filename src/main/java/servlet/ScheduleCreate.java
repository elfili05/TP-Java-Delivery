package main.java.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Time;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.entities.Restaurant;
import main.java.entities.Schedule;
import main.java.entities.User;
import main.java.logic.RestaurantCRUD;

/**
 * Servlet implementation class ScheduleCreate
 */
@WebServlet({ "/ScheduleCreate", "/schedulecreate", "/scheduleCreate", "/Schedulecreate", "/SCHEDULECREATE" })
public class ScheduleCreate extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ScheduleCreate() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("AdminRestaurants");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	// agrega un horario nuevo al restaurante y vuelve a su pantalla de edición.
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User u = (User) request.getSession().getAttribute("user");

		if (u == null || !u.getRole().equalsIgnoreCase("admin")) {
			response.sendRedirect("index.html");
			return;
		}

		Integer restaurantId = parseId(request.getParameter("restaurant_id"));
		String dayOfWeek = request.getParameter("day_of_week");
		Time startTime = parseTime(request.getParameter("start_time"));
		Time endTime = parseTime(request.getParameter("end_time"));

		RestaurantCRUD ctrlRestaurant = new RestaurantCRUD();

		Boolean created = false;
		boolean overlap = false;
		if (restaurantId != null && isValidDay(dayOfWeek) && startTime != null && endTime != null) {
			Restaurant restaurant = new Restaurant();
			restaurant.setRestaurant_id(restaurantId);

			Schedule schedule = new Schedule();
			schedule.setDay_of_week(dayOfWeek);
			schedule.setStart_time(startTime);
			schedule.setEnd_time(endTime);

			try {
				if (ctrlRestaurant.hasScheduleOverlap(restaurantId, schedule, null)) {
					overlap = true;
				} else {
					created = ctrlRestaurant.addSchedule(restaurant, schedule);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		String message;
		if (created) {
			message = "Horario agregado correctamente.";
		} else if (overlap) {
			message = "Ese horario se superpone con uno ya cargado para ese día.";
		} else {
			message = "No se pudo agregar el horario. Revisá los datos ingresados.";
		}
		request.setAttribute("message", message);
		RestaurantEdit.forwardWithRestaurant(request, response, restaurantId);
	}

	// interpreta el id de la URL/formulario; null si vino vacío o no es un número.
	static Integer parseId(String rawId) {
		try {
			return Integer.parseInt(rawId);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	// interpreta un horario "HH:mm" del formulario; null si vino vacío o mal formado.
	static Time parseTime(String rawTime) {
		if (rawTime == null || rawTime.trim().isEmpty()) {
			return null;
		}
		try {
			return Time.valueOf(rawTime.trim().length() == 5 ? rawTime.trim() + ":00" : rawTime.trim());
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	static boolean isValidDay(String dayOfWeek) {
		if (dayOfWeek == null) {
			return false;
		}
		switch (dayOfWeek.toLowerCase()) {
			case "monday": case "tuesday": case "wednesday": case "thursday":
			case "friday": case "saturday": case "sunday":
				return true;
			default:
				return false;
		}
	}

}
