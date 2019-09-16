package com.uncooleben.lab01_worldclock.hotel;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.uncooleben.lab01_worldclock.worldclock.Locale;
import com.uncooleben.lab01_worldclock.worldclock.WorldClock;
import com.uncooleben.lab01_worldclock.worldclock.impl.BeijingClock;
import com.uncooleben.lab01_worldclock.worldclock.impl.LondonClock;
import com.uncooleben.lab01_worldclock.worldclock.impl.MoscowClock;
import com.uncooleben.lab01_worldclock.worldclock.impl.NewYorkClock;
import com.uncooleben.lab01_worldclock.worldclock.impl.SydneyClock;

public class Hotel {

	private Map<Locale, WorldClock> _worldClocks;

	private String _name = ">>>Coder Hotel<<<";

	public Hotel(Date date) {
		this._worldClocks = new HashMap<Locale, WorldClock>();
		this._worldClocks.put(Locale.LONDON, new LondonClock(date));
		this._worldClocks.put(Locale.MOSCOW, new MoscowClock(date));
		this._worldClocks.put(Locale.BEIJING, new BeijingClock(date));
		this._worldClocks.put(Locale.NEW_YORK, new NewYorkClock(date));
		this._worldClocks.put(Locale.SYDNEY, new SydneyClock(date));
	}

	public String getName() {
		return this._name;
	}

	public void setWithBeijingTime(Date beijingDate) {
		for (Locale locale : this._worldClocks.keySet()) {
			this._worldClocks.get(locale).setTime(beijingDate);
		}
	}

	public Map<Locale, WorldClock> getWorldClocks() {
		return this._worldClocks;
	}
}
