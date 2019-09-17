package com.uncooleben.lab01_worldclock.hotel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import java.util.Date;

import org.junit.Test;

import com.uncooleben.lab01_worldclock.worldclock.Locale;

public class HotelTest {

	@Test
	public void testHotel() {
		Date currentDate = new Date(System.currentTimeMillis());
		Manager manager = mock(Manager.class);
		Hotel hotel = new Hotel(currentDate, manager);
		assertNotNull(hotel);
	}

	@Test
	public void testGetName() {
		Date currentDate = new Date(System.currentTimeMillis());
		Manager manager = mock(Manager.class);
		Hotel hotel = new Hotel(currentDate, manager);
		assertEquals(">>>Coder Hotel<<<", hotel.getName());
	}

	@Test
	public void testSetWithBeijingTime() {
		Date currentDate = new Date(System.currentTimeMillis());
		Manager manager = mock(Manager.class);
		Hotel hotel = new Hotel(currentDate, manager);
		hotel.setWithBeijingTime(currentDate);
		for (Locale locale : hotel.getWorldClocks().keySet()) {
			Date expectedDate = new Date(
					currentDate.getTime() - (Locale.BEIJING.getUTCOffset() - locale.getUTCOffset()) * 3600 * 1000);
			assertEquals(expectedDate, hotel.getWorldClocks().get(locale).getTime());
		}
	}

	@Test
	public void testGetWorldClocks() {
		Date currentDate = new Date(System.currentTimeMillis());
		Manager manager = mock(Manager.class);
		Hotel hotel = new Hotel(currentDate, manager);
		assertEquals(5, hotel.getWorldClocks().size());
		for (Locale locale : hotel.getWorldClocks().keySet()) {
			assertNotNull(hotel.getWorldClocks().get(locale));
			assertEquals(locale, hotel.getWorldClocks().get(locale).getLocale());
		}
	}

}
