package com.uncooleben.lab01_worldclock.worldclock;

import java.util.Date;

/**
 * This interface declares the methods a WorldClock should implement.
 * 
 * <p>
 * This is a part of SoftwareTesting-Lab01.
 * 
 * @author Juntao Peng
 * 
 */
public interface WorldClock {

	/**
	 * Gets the date of the worldclock.
	 * 
	 * @return A java.util.Date object indicating the worldclock's time
	 */
	public Date getTime();

	/**
	 * Sets the date of the worldclock according to the beijing time on manager's
	 * smartphone.
	 * 
	 * @param beijingDate A java.util.Date object indicating the beijing time on
	 *                    manager's smartphone
	 */
	public void setTime(Date beijingDate);

	/**
	 * Gets the locale of the worldclock.
	 * 
	 * @return A com.uncooleben.lab01_worldclock.worldclock.Locale object indicating
	 *         the worldclock's time zone/
	 */
	public Locale getLocale();

}
