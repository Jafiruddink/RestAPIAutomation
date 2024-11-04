package utility;

public enum Constants {
	
	Login("api/ecom/auth/login"),
	AddProduct("api/ecom/product/add-product"),
	CreateOrder("api/ecom/order/create-order"),
	DeleteOrder("api/ecom/product/delete-product");
	
	private String resource;

	Constants(String resource) {
		this.resource = resource;
	}
	
	public String getResource() {
		return resource;
	}
}
