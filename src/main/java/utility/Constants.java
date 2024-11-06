package utility;

public enum Constants {
	
	Login("api/ecom/auth/login"),
	AddProduct("api/ecom/product/add-product"),
	CreateOrder("api/ecom/order/create-order"),
	GetOrderDetails("api/ecom/order/get-orders-details"),
	DeleteProduct("api/ecom/product/delete-product"),
	DeleteOrder("api/ecom/order/delete-order");
	
	private String resource;

	Constants(String resource) {
		this.resource = resource;
	}
	
	public String getResource() {
		return resource;
	}
}
