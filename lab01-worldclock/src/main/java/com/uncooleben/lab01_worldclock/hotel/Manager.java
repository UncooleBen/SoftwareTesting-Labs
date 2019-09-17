package com.uncooleben.lab01_worldclock.hotel;

public class Manager {

	private Smartphone _smartphone;

	private String _name;

	private Hotel _employer;

	public Manager(String name, Smartphone smartphone) {
		this._smartphone = smartphone;
		this._name = name;
	}

	public Smartphone getSmartphone() {
		return this._smartphone;
	}

	public String getName() {
		return this._name;
	}

	public Hotel getEmployer() {
		return this._employer;
	}

	public void setEmployer(Hotel hotel) {
		this._employer = hotel;
	}

}
