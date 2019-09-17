package com.uncooleben.lab01_worldclock.hotel;

import java.util.Date;

public class Smartphone {

	private Date _date;
	private String _name;
	private Manager _owner;

	public Smartphone(String name, Date date) {
		this._name = name;
		this._date = date;
	}

	public Date getTime() {
		return this._date;
	}

	public String getName() {
		return this._name;
	}

	public void setTime(Date date) {
		this._date = date;
		this.getOwner().getEmployer().setWithBeijingTime(date);
	}

	public void setOwner(Manager manager) {
		this._owner = manager;
	}

	public Manager getOwner() {
		return this._owner;
	}

}
