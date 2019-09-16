package com.uncooleben.lab01_worldclock.worldclock.impl;

import java.util.Date;

import com.uncooleben.lab01_worldclock.worldclock.Locale;
import com.uncooleben.lab01_worldclock.worldclock.WorldClock;

public class LondonClock implements WorldClock {

	private Date _date;
	private Locale _locale = Locale.LONDON;

	public LondonClock(Date date) {
		this._date = date;
	}

	public Date getTime() {
		return this._date;
	}

	public void setTime(Date beijingDate) {
		long UTCTime = beijingDate.getTime() - Locale.BEIJING.getUTCOffset() * 3600 * 1000;
		this._date = new Date(UTCTime + this._locale.getUTCOffset() * 3600 * 1000);
	}

	public Locale getLocale() {
		return this._locale;
	}

}