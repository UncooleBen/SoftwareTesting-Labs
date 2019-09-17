package com.uncooleben.lab01_worldclock.hotel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import java.util.Date;

import org.junit.Test;

public class SmartphoneTest {

	@Test
	public void smartphoneConstructorTest() {
		Date currentDate = new Date(System.currentTimeMillis());
		Smartphone smartphone = new Smartphone("Test Smartphone", currentDate);
		assertNotNull(smartphone);
	}

	@Test
	public void testGetTime() {
		Date currentDate = new Date(System.currentTimeMillis());
		Smartphone smartphone = new Smartphone("Test Smartphone", currentDate);
		assertEquals(currentDate, smartphone.getTime());
	}

	@Test
	public void testGetName() {
		Date currentDate = new Date(System.currentTimeMillis());
		Smartphone smartphone = new Smartphone("Test Smartphone", currentDate);
		assertEquals("Test Smartphone", smartphone.getName());
	}

	@Test
	public void testSetTime() {
		Date currentDate = new Date(System.currentTimeMillis());
		Smartphone smartphone = new Smartphone("Test Smartphone", currentDate);
		assertEquals(currentDate, smartphone.getTime());
	}

	@Test
	public void testSetOwner() {
		Manager manager = mock(Manager.class);
		Date currentDate = new Date(System.currentTimeMillis());
		Smartphone smartphone = new Smartphone("Test Smartphone", currentDate);
		smartphone.setOwner(manager);
		assertEquals(manager, smartphone.getOwner());
	}

	@Test
	public void testGetOwner() {
		Manager manager = mock(Manager.class);
		Date currentDate = new Date(System.currentTimeMillis());
		Smartphone smartphone = new Smartphone("Test Smartphone", currentDate);
		smartphone.setOwner(manager);
		assertEquals(manager, smartphone.getOwner());
	}

}
