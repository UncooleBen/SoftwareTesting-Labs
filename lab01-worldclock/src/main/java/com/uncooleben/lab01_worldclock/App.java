package com.uncooleben.lab01_worldclock;

import java.util.Date;

import com.uncooleben.lab01_worldclock.hotel.Hotel;
import com.uncooleben.lab01_worldclock.hotel.Manager;
import com.uncooleben.lab01_worldclock.hotel.Smartphone;
import com.uncooleben.lab01_worldclock.worldclock.Locale;

public class App {
	public static void main(String[] args) {
		Date date = new Date(System.currentTimeMillis());
		Smartphone smartphone = new Smartphone("iPhone 11 Pro Max", date);
		Manager manager = new Manager("Paul Hilfinger", smartphone);
		smartphone.setOwner(manager);
		Hotel hotel = new Hotel(date, manager);
		manager.setEmployer(hotel);
		System.out.println("Welcome to " + hotel.getName());
		System.out.println("Current world clock is:");
		printWorldClocks(hotel);
		System.out.println("Setting smartphone time...");
		smartphone.setTime(new Date(System.currentTimeMillis()));
		System.out.println("Current world clock is:");
		printWorldClocks(hotel);
	}

	private static void printWorldClocks(Hotel hotel) {
		for (Locale locale : hotel.getWorldClocks().keySet()) {
			System.out.println(locale.toString() + " time is " + hotel.getWorldClocks().get(locale).getTime());
		}
		System.out.println("=========================");
	}
}
