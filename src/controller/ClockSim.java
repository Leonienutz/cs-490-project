package controller;

/**
 * Class simulating a system clock that can be referenced by any objects
 * that need to use a synchronized clock.
 */
public class ClockSim {
	private long currentTime = 0;
	private long timeLastUpdated = 0;
	private boolean running = false;
	
	/**
	 * Constructor for the ClockSim.
	 */
	public ClockSim() {
		
	}
	
	/**
	 * Increments the clock based how much time has passed since it was last updated.
	 * If the clock's running flag is set to false then this does nothing instead.
	 */
	public void updateClock() {
		if (running) {
			long temp = System.currentTimeMillis();
			currentTime = currentTime + (temp - timeLastUpdated);
			timeLastUpdated = temp;
		}
	}
	
	/**
	 * Sets the clock's running flag to false.
	 */
	public void pauseClock() {
		running = false;
	}
	
	/**
	 * Updates the clock and sets its running flag to true.
	 */
	public void resumeClock() {
		long temp = System.currentTimeMillis();
		timeLastUpdated = temp;
		currentTime = currentTime + (temp - timeLastUpdated);
		running = true;
	}
	
	/**
	 * Gets the current elapsed time since the clock was started in milliseconds.
	 * @return currentTime
	 */
	public long getCurrentTime() {
		return currentTime;
	}
}
