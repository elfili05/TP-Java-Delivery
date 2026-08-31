package main.java.entities;

public class Discount {
	
	private int discount_id;
	private double minimum_amount;
	private double discount_percentage;
	
	public int getDiscount_id() {
		return discount_id;
	}

	public void setDiscount_id(int discount_id) {
		this.discount_id = discount_id;
	}

	public double getMinimum_amount() {
		return minimum_amount;
	}

	public void setMinimum_amount(double minimum_amount) {
		this.minimum_amount = minimum_amount;
	}

	public double getDiscount_percentage() {
		return discount_percentage;
	}

	public void setDiscount_percentage(double discount_percentage) {
		this.discount_percentage = discount_percentage;
	}
	
	public Discount() {
		this.discount_id = 0;
		this.minimum_amount = 0.0;
		this.discount_percentage = 0.0;
	}
}
