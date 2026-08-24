package java.entities;

public class Discount {
	
	private int discount_id;
	private double minimum_amount;
	private double discount_percentage;
	
	public int getDiscount_id() {
		return discount_id;
	}

	void setDiscount_id(int discount_id) {
		this.discount_id = discount_id;
	}

	double getMinimum_amount() {
		return minimum_amount;
	}

	void setMinimum_amount(double minimum_amount) {
		this.minimum_amount = minimum_amount;
	}

	double getDiscount_percentage() {
		return discount_percentage;
	}

	void setDiscount_percentage(double discount_percentage) {
		this.discount_percentage = discount_percentage;
	}
}
