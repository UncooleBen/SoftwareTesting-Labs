package com.uncooleben.lab01_worldclock.worldclock.impl;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

import com.uncooleben.lab01_worldclock.worldclock.Locale;
import com.uncooleben.lab01_worldclock.worldclock.WorldClock;

public class SydneyClockTest {

	@Test
	public void SydneyClockConstructorTest() {
		Date date = new Date(System.currentTimeMillis());
		WorldClock clock = new SydneyClock(date);
		assertEquals(date, clock.getTime());
		assertEquals(Locale.SYDNEY, clock.getLocale());
	}

	@Test
	public void GetTimeTest() {
		Date date = new Date(System.currentTimeMillis());
		WorldClock clock = new SydneyClock(date);
		assertEquals(date, clock.getTime());
	}

	@Test
	public void SetTimeTest() {
		Date beijingDate = new Date(System.currentTimeMillis());
		WorldClock clock = new SydneyClock(beijingDate);
		clock.setTime(beijingDate);
		Date sydneyDate = new Date(
				beijingDate.getTime() - (Locale.BEIJING.getUTCOffset() - Locale.SYDNEY.getUTCOffset()) * 3600 * 1000);
		assertEquals(sydneyDate, clock.getTime());
	}

	@Test
	public void GetLocaleTest() {
		Date date = new Date(System.currentTimeMillis());
		WorldClock clock = new SydneyClock(date);
		assertEquals(Locale.SYDNEY, clock.getLocale());
	}

}
