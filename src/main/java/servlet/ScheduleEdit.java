package main.java.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Time;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.entities.Schedule;
import main.java.entities.User;
import main.java.logic.RestaurantCRUD;

/**
 * Servlet implementation class ScheduleEdit
 */
@WebServlet({ "/ScheduleEdit", "/scheduleedit", "/scheduleEdit", "/Scheduleedit", "/SCHEDULEEDIT" })
public class ScheduleEdit extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ScheduleEdit() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	// busca el horario elegido (por restaurant_id + schedule_number) y precarga el formulario.
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User u = (User) request.getSession().getAttribute("user");

		if (u == null || !u.getRole().equalsIgnoreCase("admin")) {
			response.sendRedirect("index.html");
			return;
		}

		Integer restaurantId = ScheduleCreate.parseId(request.getParameter("restaurant_id"));
		Integer scheduleNumber = ScheduleCreate.parseId(request.getParameter("schedule_number"));
		if (restaurantId == null || scheduleNumber == null) {
			response.sendRedirect("AdminRestaurants");
			return;
		}

		Schedule scheduleToEdit = null;
		try {
			for (Schedule candidate : new RestaurantCRUD().getSchedules(restaurantId)) {
				if (candidate.getSchedule_number() == scheduleNumber) {
					scheduleToEdit = candidate;
					break;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (scheduleToEdit == null) {
			response.sendRedirect("RestaurantEdit?id=" + restaurantId);
			return;
		}

		request.setAttribute("editedSchedule", scheduleToEdit);
		request.getRequestDispatcher("WEB-INF/admin_schedule_edit.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	// guarda los cambios del horario y vuelve a la pantalla de edición del restaurante.
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User u = (User) request.getSession().getAttribute("user");

		if (u == null || !u.getRole().equalsIgnoreCase("admin")) {
			response.sendRedirect("index.html");
			return;
		}

		Integer restaurantId = ScheduleCreate.parseId(request.getParameter("restaurant_id"));
		Integer scheduleNumber = ScheduleCreate.parseId(request.getParameter("schedule_number"));
		String dayOfWeek = request.getParameter("day_of_week");
		Time startTime = ScheduleCreate.parseTime(request.getParameter("start_time"));
		Time endTime = ScheduleCreate.parseTime(request.getParameter("end_time"));

		RestaurantCRUD ctrlRestaurant = new RestaurantCRUD();

		Boolean updated = false;
		boolean overlap = false;
		if (restaurantId != null && scheduleNumber != null && ScheduleCreate.isValidDay(dayOfWeek) && startTime != null && endTime != null) {
			Schedule schedule = new Schedule();
			schedule.setRestaurant_id(restaurantId);
			schedule.setSchedule_number(scheduleNumber);
			schedule.setDay_of_week(dayOfWeek);
			schedule.setStart_time(startTime);
			schedule.setEnd_time(endTime);

			try {
				if (ctrlRestaurant.hasScheduleOverlap(restaurantId, schedule, scheduleNumber)) {
					overlap = true;
				} else {
					updated = ctrlRestaurant.updateSchedule(schedule);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		String message;
		if (updated) {
			message = "Horario actualizado correctamente.";
		} else if (overlap) {
			message = "Ese horario se superpone con uno ya cargado para ese día.";
		} else {
			message = "No se pudo actualizar el horario.";
		}
		request.setAttribute("message", message);
		RestaurantEdit.forwardWithRestaurant(request, response, restaurantId);
	}

}
