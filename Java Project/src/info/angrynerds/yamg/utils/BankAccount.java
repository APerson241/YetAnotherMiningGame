package info.angrynerds.yamg.utils;

import java.beans.*;
import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class BankAccount implements Serializable {
	private int money;
	
	private ArrayList<PropertyChangeListener> listeners;
	
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
		this(500);
	}
	
	public int getMoney() {
		return money;
	}
	
	public void deposit(int value) {
		if(value >= 0) {
			notifyPropertyChangeListeners(money, money + value);
			money += value;
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
}