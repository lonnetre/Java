package vsue.rpc;

import java.io.Serializable;

// TODO: VSAuction... - Anwendung (darf objecte exportieren)
// TODO: Andere - Fernaufrufsystem (darf nicht exportieren)

public class VSAuction implements Serializable {
	private final String name;
	private volatile int price;

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

	@Override
	public boolean equals (Object object) {
		if (this == object) return true;
		if (object == null || getClass() != object.getClass()) return false;
		VSAuction that = (VSAuction) object;
		return this.getName().equals(that.getName());
	}

	@Override
	public int hashCode() {
		return getName().hashCode();
	}
	
}
