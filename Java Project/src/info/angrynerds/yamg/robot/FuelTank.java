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
}