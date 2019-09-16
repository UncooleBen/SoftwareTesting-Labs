package com.uncooleben.lab01_worldclock;

import java.util.Date;

import com.uncooleben.lab01_worldclock.hotel.Hotel;
import com.uncooleben.lab01_worldclock.worldclock.Locale;

public class App {
	public static void main(String[] args) {
		Date date = new Date(System.currentTimeMillis());
		Hotel hotel = new Hotel(date);
		System.out.println("Welcome to " + hotel.getName());
		System.out.println("Current world clock is:");
		printWorldClocks(hotel);
		System.out.println("Winding clock...");
		hotel.setWithBeijingTime(new Date(System.currentTimeMillis()));
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
