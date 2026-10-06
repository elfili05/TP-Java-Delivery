package main.java.logic;

import java.sql.SQLException;
import java.util.LinkedList;

import main.java.data.ProductRepository;
import main.java.data.ProductTypeRepository;
import main.java.entities.Product;
import main.java.entities.ProductType;
import main.java.entities.Restaurant;

public class ProductCRUD {

	private ProductRepository pr;
	private ProductTypeRepository ptr;

	public ProductCRUD() {
		pr = new ProductRepository();
		ptr = new ProductTypeRepository();
	}

	public LinkedList<Product> getProducts(Restaurant restaurant) throws SQLException {
		return pr.getAll(restaurant);
	}

	public Product getProduct(int productId) throws SQLException {
		return pr.getOne(productId);
	}

	public LinkedList<ProductType> getProductTypes() throws SQLException {
		return ptr.getAll();
	}

	public Boolean addProduct(Product product, Restaurant restaurant, ProductType productType) throws SQLException {
		return pr.addProduct(product, restaurant, productType);
	}

	public Boolean updateProduct(Product product, ProductType productType) throws SQLException {
		return pr.updateProduct(product, productType);
	}

	public Boolean deleteProduct(int productId) throws SQLException {
		return pr.deleteProduct(productId);
	}

}
