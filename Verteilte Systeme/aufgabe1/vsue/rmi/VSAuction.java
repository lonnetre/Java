package vsue.rmi;

import java.io.Serializable;

// Serializable: siehe Folie 1.2:7 (Aufgabe 1: RMI)
public class VSAuction implements Serializable {

	/* The auction name. */
	private final String name;

	/* The currently highest bid for this auction. */
	private int price;

	public VSAuction(String name, int startingPrice) {
		this.name = name;
		this.price = startingPrice;
	}

	public String getName() {
		return name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	// simple equals is not enough for VSAuction
	@Override
	public boolean equals (Object object) {
		return ((VSAuction)object).getName().equals(this.getName()) ? true : false;
	}
	
}