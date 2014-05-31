package info.angrynerds.yamg.environment;

public enum ElementType {
	COPPER(10),
	ALUMINUM(50),
	SILVER(75),
	GOLD(100),
	BISMUTH(200),
	PLATINUM(500),
	EINSTEINIUM(1000);
	
	private int value;
	
	ElementType(int val) {
		value = val;
	}
	
	public int getPrice() {
		return value;
	}
}
