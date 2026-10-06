package main.java.logic;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.LinkedList;

import main.java.data.ProductTypeRepository;
import main.java.entities.ProductType;

public class ProductTypeCRUD {

	private ProductTypeRepository ptr;

	public ProductTypeCRUD() {
		ptr = new ProductTypeRepository();
	}

	public LinkedList<ProductType> getProductTypes() throws SQLException {
		return ptr.getAll();
	}

	public ProductType getProductType(int productTypeId) throws SQLException {
		return ptr.getOne(productTypeId);
	}

	// los métodos de escritura devuelven null si salieron bien; si no, el mensaje de error para mostrar al admin.
	public String addProductType(String rawName) throws SQLException {
		String nameError = validateName(rawName, null);
		if (nameError != null) {
			return nameError;
		}

		ProductType productType = new ProductType();
		productType.setName(rawName.trim());
		try {
			return ptr.addProductType(productType) ? null : "No se pudo crear el tipo de producto.";
		} catch (SQLIntegrityConstraintViolationException e) {
			return "Ya existe un tipo de producto con ese nombre.";
		}
	}

	public String updateProductType(int productTypeId, String rawName) throws SQLException {
		String nameError = validateName(rawName, productTypeId);
		if (nameError != null) {
			return nameError;
		}

		ProductType productType = new ProductType();
		productType.setProduct_type_id(productTypeId);
		productType.setName(rawName.trim());
		try {
			return ptr.updateProductType(productType) ? null : "No se pudo actualizar el tipo de producto.";
		} catch (SQLIntegrityConstraintViolationException e) {
			return "Ya existe un tipo de producto con ese nombre.";
		}
	}

	public String deleteProductType(int productTypeId) throws SQLException {
		try {
			return ptr.deleteProductType(productTypeId) ? null : "No se pudo eliminar el tipo de producto.";
		} catch (SQLIntegrityConstraintViolationException e) {
			return "No se puede eliminar: hay productos de este tipo.";
		}
	}

	// el nombre no se repite ignorando mayúsculas/minúsculas; excludeId evita que al editar se compare contra sí mismo.
	private String validateName(String rawName, Integer excludeId) throws SQLException {
		if (rawName == null || rawName.trim().isEmpty()) {
			return "El nombre es obligatorio.";
		}
		if (rawName.trim().length() > 80) {
			return "El nombre no puede superar los 80 caracteres.";
		}
		for (ProductType existing : ptr.getAll()) {
			if (excludeId != null && existing.getProduct_type_id() == excludeId) {
				continue;
			}
			if (existing.getName() != null && existing.getName().equalsIgnoreCase(rawName.trim())) {
				return "Ya existe un tipo de producto con ese nombre.";
			}
		}
		return null;
	}

}
