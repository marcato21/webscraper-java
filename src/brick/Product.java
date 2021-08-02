package brick;

import java.math.BigDecimal;

public class Product {

	//	Product attributes
	String name;
	String description;
	String imgUrl;
	BigDecimal price;
	Float rating;
	String store;

	public Product(String name, String description, String imgUrl, BigDecimal price, Float rating, String store) {
		this.name = name;
		this.description = description;
		this.imgUrl = imgUrl;
		this.price = price;
		this.rating = rating;
		this.store = store;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Float getRating() {
		return rating;
	}

	public void setRating(Float rating) {
		this.rating = rating;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}
}
