package com.uncooleben.lab01_worldclock.worldclock.impl;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

import com.uncooleben.lab01_worldclock.worldclock.Locale;
import com.uncooleben.lab01_worldclock.worldclock.WorldClock;

public class LondonClockTest {

	@Test
	public void LondonClockConstructorTest() {
		Date date = new Date(System.currentTimeMillis());
		WorldClock clock = new LondonClock(date);
		assertEquals(date, clock.getTime());
		assertEquals(Locale.LONDON, clock.getLocale());
	}

	@Test
	public void GetTimeTest() {
		Date date = new Date(System.currentTimeMillis());
		WorldClock clock = new LondonClock(date);
		assertEquals(date, clock.getTime());
	}

	@Test
	public void SetTimeTest() {
		Date beijingDate = new Date(System.currentTimeMillis());
		WorldClock clock = new LondonClock(beijingDate);
		clock.setTime(beijingDate);
		Date londonDate = new Date(
				beijingDate.getTime() - (Locale.BEIJING.getUTCOffset() - Locale.LONDON.getUTCOffset()) * 3600 * 1000);
		assertEquals(londonDate, clock.getTime());
	}

	@Test
	public void GetLocaleTest() {
		Date date = new Date(System.currentTimeMillis());
		WorldClock clock = new LondonClock(date);
		assertEquals(Locale.LONDON, clock.getLocale());
	}

}
