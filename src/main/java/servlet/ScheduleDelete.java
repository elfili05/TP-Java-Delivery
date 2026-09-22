package main.java.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.entities.User;
import main.java.logic.RestaurantCRUD;

/**
 * Servlet implementation class ScheduleDelete
 */
@WebServlet({ "/ScheduleDelete", "/scheduledelete", "/scheduleDelete", "/Scheduledelete", "/SCHEDULEDELETE" })
public class ScheduleDelete extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ScheduleDelete() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	// borra el horario indicado y vuelve a la pantalla de edición del restaurante.
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User u = (User) request.getSession().getAttribute("user");

		if (u == null || !u.getRole().equalsIgnoreCase("admin")) {
			response.sendRedirect("index.html");
			return;
		}

		Integer restaurantId = ScheduleCreate.parseId(request.getParameter("restaurant_id"));
		Integer scheduleNumber = ScheduleCreate.parseId(request.getParameter("schedule_number"));

		RestaurantCRUD ctrlRestaurant = new RestaurantCRUD();

		Boolean deleted = false;
		if (restaurantId != null && scheduleNumber != null) {
			try {
				deleted = ctrlRestaurant.deleteSchedule(restaurantId, scheduleNumber);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		request.setAttribute("message", deleted ? "Horario eliminado correctamente." : "No se pudo eliminar el horario.");
		RestaurantEdit.forwardWithRestaurant(request, response, restaurantId);
	}

}
