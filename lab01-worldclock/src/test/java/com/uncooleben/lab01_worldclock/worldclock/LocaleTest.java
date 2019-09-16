package com.uncooleben.lab01_worldclock.worldclock;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LocaleTest {

	@Test
	public void BeijingTest() {
		assertEquals(+8, Locale.BEIJING.getUTCOffset());
	}

	@Test
	public void LondonTest() {
		assertEquals(0, Locale.LONDON.getUTCOffset());
	}

	@Test
	public void MoscowTest() {
		assertEquals(+4, Locale.MOSCOW.getUTCOffset());
	}

	@Test
	public void SydneyTest() {
		assertEquals(+10, Locale.SYDNEY.getUTCOffset());
	}

	@Test
	public void NewYorkTest() {
		assertEquals(-5, Locale.NEW_YORK.getUTCOffset());
	}

}
