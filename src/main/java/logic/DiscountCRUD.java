package main.java.logic;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.LinkedList;

import main.java.data.DiscountRepository;
import main.java.entities.Discount;

public class DiscountCRUD {

	private DiscountRepository dr;

	public DiscountCRUD() {
		dr = new DiscountRepository();
	}

	public LinkedList<Discount> getDiscounts() throws SQLException {
		return dr.getAll();
	}

	public Discount getDiscount(int discountId) throws SQLException {
		return dr.getById(discountId);
	}

	// los métodos de escritura devuelven null si salieron bien; si no, el mensaje de error para mostrar al admin.
	public String addDiscount(String rawMinimumAmount, String rawPercentage) throws SQLException {
		String validationError = validateFields(rawMinimumAmount, rawPercentage);
		if (validationError != null) {
			return validationError;
		}

		Discount discount = buildDiscount(0, rawMinimumAmount, rawPercentage);
		try {
			return dr.addDiscount(discount) ? null : "No se pudo crear el descuento.";
		} catch (SQLIntegrityConstraintViolationException e) {
			return "Ya existe un descuento con ese monto mínimo.";
		}
	}

	public String updateDiscount(int discountId, String rawMinimumAmount, String rawPercentage) throws SQLException {
		String validationError = validateFields(rawMinimumAmount, rawPercentage);
		if (validationError != null) {
			return validationError;
		}

		// un descuento que ya usan pedidos no se modifica: el historial de esos pedidos cambiaría de monto retroactivamente.
		if (dr.countOrdersUsing(discountId) > 0) {
			return "No se puede modificar: hay pedidos que usan este descuento. Creá uno nuevo.";
		}

		Discount discount = buildDiscount(discountId, rawMinimumAmount, rawPercentage);
		try {
			return dr.updateDiscount(discount) ? null : "No se pudo actualizar el descuento.";
		} catch (SQLIntegrityConstraintViolationException e) {
			return "Ya existe un descuento con ese monto mínimo.";
		}
	}

	public String deleteDiscount(int discountId) throws SQLException {
		try {
			return dr.deleteDiscount(discountId) ? null : "No se pudo eliminar el descuento.";
		} catch (SQLIntegrityConstraintViolationException e) {
			return "No se puede eliminar: hay pedidos que usan este descuento.";
		}
	}

	private String validateFields(String rawMinimumAmount, String rawPercentage) {
		Double minimumAmount = parseDecimal(rawMinimumAmount);
		if (minimumAmount == null || minimumAmount <= 0) {
			return "El monto mínimo debe ser un número mayor a 0, con hasta 2 decimales y sin separador de miles (ej.: 30000 o 1500,50).";
		}
		Double percentage = parseDecimal(rawPercentage);
		if (percentage == null || percentage <= 0 || percentage > 100) {
			return "El porcentaje debe ser un número mayor a 0 y hasta 100.";
		}
		return null;
	}

	// el porcentaje se ingresa como % (10) pero se guarda como fracción (0.1), que es lo que espera Order.getTotalWithDiscount().
	private Discount buildDiscount(int discountId, String rawMinimumAmount, String rawPercentage) {
		Discount discount = new Discount();
		discount.setDiscount_id(discountId);
		discount.setMinimum_amount(parseDecimal(rawMinimumAmount));
		discount.setDiscount_percentage(parseDecimal(rawPercentage) / 100);
		return discount;
	}

	// acepta coma o punto como separador decimal, con hasta 2 decimales: así "30.000" o "30,000" (escritos como miles) se rechazan
	// en vez de guardarse como 30. Devuelve null si vino vacío o no es un número finito.
	// el formato se valida antes porque Double.parseDouble también acepta cosas como "1d", "1e3" o "0x10p1".
	private Double parseDecimal(String rawValue) {
		if (rawValue == null || !rawValue.trim().matches("\\d{1,9}([.,]\\d{1,2})?")) {
			return null;
		}
		try {
			double value = Double.parseDouble(rawValue.trim().replace(',', '.'));
			return Double.isFinite(value) ? value : null;
		} catch (NumberFormatException e) {
			return null;
		}
	}

}
