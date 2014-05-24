package info.angrynerds.yamg.utils;

import java.beans.*;
import java.io.Serializable;
import java.util.*;

@SuppressWarnings("serial")
public class BankAccount implements Serializable {
	private int money;
	
	private List<PropertyChangeListener> listeners;
	
	public BankAccount(int amount) {
		money = amount;
		listeners = new ArrayList<PropertyChangeListener>();
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		listeners.add(listener);
	}
	
	public void notifyPropertyChangeListeners(int oldMoney, int newMoney) {
		PropertyChangeEvent event = new PropertyChangeEvent(this, "money", oldMoney, newMoney);
		for(PropertyChangeListener listener:listeners) {
			listener.propertyChange(event);
		}
	}
	
	public BankAccount() {
		this(Configurables.STARTING_MONEY);
	}
	
	public int getMoney() {
		return money;
	}
	
	public void deposit(int value) {
		if(value >= 0) {
			notifyPropertyChangeListeners(money, money + value);
			money += value;
			DebugConsole.getInstance().println("[BankAccount/deposit()] Deposited " + value);
		} else {
			throw new IllegalArgumentException("You can't deposit a negative number of dollars!");
		}
	}
	
	public void withdraw(int value) {
		if(value < 0) {
			throw new IllegalArgumentException("You can't withdraw a negative number dollars!");
		} else if(value > money) {
			throw new IllegalArgumentException("You don't have enough money to withdraw " +
					value + "dollars!");
		} else {
			notifyPropertyChangeListeners(money, money - value);
			money -= value;
		}
	}
	
	public void setMoney(int value) {
		notifyPropertyChangeListeners(money, value);
		money = value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((listeners == null) ? 0 : listeners.hashCode());
		result = prime * result + money;
		return result;
	}

	/* (non-Javadoc)
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
		BankAccount other = (BankAccount) obj;
		if (listeners == null) {
			if (other.listeners != null)
				return false;
		} else if (!listeners.equals(other.listeners))
			return false;
		if (money != other.money)
			return false;
		return true;
	}
}