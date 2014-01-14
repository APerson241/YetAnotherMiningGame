package info.angrynerds.yamg.robot;

public class FuelTank {
	private int fuelLevel;
	private int fuelCapacity;
	private int tier;
	
	private boolean infinite = false;

	public FuelTank() {
		fuelLevel = 40;
		fuelCapacity = 40;
		tier = 1;
	}

	public int getFuelLevel() {
		return fuelLevel;
	}
	
	public boolean isInfinite() {
		return infinite;
	}

	public void setFuelLevel(int fuelLevel) {
		this.fuelLevel = fuelLevel;
	}
	
	public void useFuel(int amount) {
		if((amount <= fuelLevel) && (amount > 0) && !infinite) {
			fuelLevel -= amount;
		}
	}
	
	public void refuel(int amount) {
		if((amount > 0) && ((amount + fuelLevel) <= fuelCapacity) && !infinite) {
			fuelLevel += amount;
		}
	}

	public int getFuelCapacity() {
		return fuelCapacity;
	}
	
	public int getFuelPercent() {
		if(fuelCapacity <= 100) {
			double factor = 100 / fuelCapacity;
			//		System.out.print("Factor = " + factor);
			double result = ((fuelLevel * factor) / (fuelCapacity * factor)) * 100;
			//		System.out.println(", Result = " + result);
			return (int) result;
		} else {
			double result = (fuelLevel * 100) / fuelCapacity;
			//		System.out.println("Result = " + result);
			return (int) (result);
		}
	}

	public void setFuelCapacity(int fuelCapacity) {
		this.fuelCapacity = fuelCapacity;
	}
	
	public int getTier() {
		return tier;
	}
	
	public void upgrade() {
		if(tier < 19) {
			fuelCapacity += 20;
			fuelLevel += 20;
			tier++;
		} else {
			tier = 20;
			fuelCapacity = 500;
			fuelLevel = 480;
		}
	}
	
	public int getMoneyNeededForNextTier() {
		return 10 + (((int)Math.pow(getTier() + 1, 2)) * 10);
	}

	public boolean isFull() {
		return fuelLevel == fuelCapacity;
	}

	public void infiniteFuel(boolean b) {
		infinite = b;
	}

	public boolean isEmpty() {
		return fuelLevel == 0;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + fuelCapacity;
		result = prime * result + fuelLevel;
		result = prime * result + (infinite ? 1231 : 1237);
		result = prime * result + tier;
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FuelTank other = (FuelTank) obj;
		if (fuelCapacity != other.fuelCapacity)
			return false;
		if (fuelLevel != other.fuelLevel)
			return false;
		if (infinite != other.infinite)
			return false;
		if (tier != other.tier)
			return false;
		return true;
	}
}