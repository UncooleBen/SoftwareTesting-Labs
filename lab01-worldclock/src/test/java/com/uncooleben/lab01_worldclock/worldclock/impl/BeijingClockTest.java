package com.uncooleben.lab01_worldclock.worldclock.impl;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

import com.uncooleben.lab01_worldclock.worldclock.Locale;
import com.uncooleben.lab01_worldclock.worldclock.WorldClock;

public class BeijingClockTest {

	@Test
	public void BeijingClockConstructorTest() {
		Date date = new Date(System.currentTimeMillis());
		WorldClock clock = new BeijingClock(date);
		assertEquals(date, clock.getTime());
		assertEquals(Locale.BEIJING, clock.getLocale());
	}

	@Test
	public void GetTimeTest() {
		Date date = new Date(System.currentTimeMillis());
		WorldClock clock = new BeijingClock(date);
		assertEquals(date, clock.getTime());
	}

	@Test
	public void SetTimeTest() {
		Date date = new Date(System.currentTimeMillis());
		WorldClock clock = new BeijingClock(date);
		clock.setTime(date);
		assertEquals(date, clock.getTime());
	}

	@Test
	public void GetLocaleTest() {
		Date date = new Date(System.currentTimeMillis());
		WorldClock clock = new BeijingClock(date);
		assertEquals(Locale.BEIJING, clock.getLocale());
	}

}
