package com.uncooleben.lab01_worldclock.worldclock.impl;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

import com.uncooleben.lab01_worldclock.worldclock.Locale;
import com.uncooleben.lab01_worldclock.worldclock.WorldClock;

public class NewYorkClockTest {

	@Test
	public void NewYorkClockConstructorTest() {
		Date date = new Date(System.currentTimeMillis());
		WorldClock clock = new NewYorkClock(date);
		assertEquals(date, clock.getTime());
		assertEquals(Locale.NEW_YORK, clock.getLocale());
	}

	@Test
	public void GetTimeTest() {
		Date date = new Date(System.currentTimeMillis());
		WorldClock clock = new NewYorkClock(date);
		assertEquals(date, clock.getTime());
	}

	@Test
	public void SetTimeTest() {
		Date beijingDate = new Date(System.currentTimeMillis());
		WorldClock clock = new NewYorkClock(beijingDate);
		clock.setTime(beijingDate);
		Date newyorkDate = new Date(
				beijingDate.getTime() - (Locale.BEIJING.getUTCOffset() - Locale.NEW_YORK.getUTCOffset()) * 3600 * 1000);
		assertEquals(newyorkDate, clock.getTime());
	}

	@Test
	public void GetLocaleTest() {
		Date date = new Date(System.currentTimeMillis());
		WorldClock clock = new NewYorkClock(date);
		assertEquals(Locale.NEW_YORK, clock.getLocale());
	}

}
