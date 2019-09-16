package com.uncooleben.lab01_worldclock.worldclock.impl;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

import com.uncooleben.lab01_worldclock.worldclock.Locale;
import com.uncooleben.lab01_worldclock.worldclock.WorldClock;

public class MoscowClockTest {

	@Test
	public void MoscowClockConstructorTest() {
		Date date = new Date(System.currentTimeMillis());
		WorldClock clock = new MoscowClock(date);
		assertEquals(date, clock.getTime());
		assertEquals(Locale.MOSCOW, clock.getLocale());
	}

	@Test
	public void GetTimeTest() {
		Date date = new Date(System.currentTimeMillis());
		WorldClock clock = new MoscowClock(date);
		assertEquals(date, clock.getTime());
	}

	@Test
	public void SetTimeTest() {
		Date beijingDate = new Date(System.currentTimeMillis());
		WorldClock clock = new MoscowClock(beijingDate);
		clock.setTime(beijingDate);
		Date moscowDate = new Date(
				beijingDate.getTime() - (Locale.BEIJING.getUTCOffset() - Locale.MOSCOW.getUTCOffset()) * 3600 * 1000);
		assertEquals(moscowDate, clock.getTime());
	}

	@Test
	public void GetLocaleTest() {
		Date date = new Date(System.currentTimeMillis());
		WorldClock clock = new MoscowClock(date);
		assertEquals(Locale.MOSCOW, clock.getLocale());
	}

}
