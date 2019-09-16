package com.uncooleben.lab01_worldclock.worldclock;

public enum Locale {

	BEIJING(+8), LONDON(+0), NEW_YORK(-5), MOSCOW(+4), SYDNEY(+10);

	private final int _UTCOffset;

	private Locale(int UTCOffset) {
		this._UTCOffset = UTCOffset;
	}

	public int getUTCOffset() {
		return this._UTCOffset;
	}
}
