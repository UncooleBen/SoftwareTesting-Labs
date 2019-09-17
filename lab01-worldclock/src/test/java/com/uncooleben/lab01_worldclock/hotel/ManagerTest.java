package com.uncooleben.lab01_worldclock.hotel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import org.junit.Test;

public class ManagerTest {

	@Test
	public void managerConstructorTest() {
		Smartphone smartphone = mock(Smartphone.class);
		Manager manager = new Manager("Test Manager", smartphone);
		assertNotNull(manager);
	}

	@Test
	public void testGetSmartphone() {
		Smartphone smartphone = mock(Smartphone.class);
		Manager manager = new Manager("Test Manager", smartphone);
		assertEquals(smartphone, manager.getSmartphone());
	}

	@Test
	public void testGetName() {
		Smartphone smartphone = mock(Smartphone.class);
		Manager manager = new Manager("Test Manager", smartphone);
		assertEquals("Test Manager", manager.getName());
	}

	@Test
	public void testGetEmployer() {
		Smartphone smartphone = mock(Smartphone.class);
		Hotel hotel = mock(Hotel.class);
		Manager manager = new Manager("Test Manager", smartphone);
		manager.setEmployer(hotel);
		assertEquals(hotel, manager.getEmployer());
	}

	@Test
	public void testSetEmployer() {
		Smartphone smartphone = mock(Smartphone.class);
		Hotel hotel = mock(Hotel.class);
		Manager manager = new Manager("Test Manager", smartphone);
		manager.setEmployer(hotel);
		assertEquals(hotel, manager.getEmployer());
	}

}
