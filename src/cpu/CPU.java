package cpu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;

/**
 * Class representing a CPU. The CPU can be passed a process which is
 * "executed" based on its service time.
 */
public class CPU extends Thread {
	private boolean running;
	private boolean processing;
	private ArrayList<ActionListener> actionListeners;
	private long currentServiceTime;
	private long currentProcessServiceTime;
	private long lastTimeUpdate = 0;
	private String currentProcessName;
	private int timeUnit;
	private ProcessSim currProcess;

	/**
	 * Constructor for the CPU.
	 */
	public CPU() {
		running = false;
		processing = false;
		actionListeners = new ArrayList<ActionListener>();
		currentServiceTime = 0;
		currentProcessName = "None";
		currentProcessServiceTime = 0;
		timeUnit = 100;
	}
	
	/**
	 * Runs the CPU's main loop. The loop runs forever. If the current process is not null
	 * then the CPU will increment a clock until the it reaches the processe's service time.
	 */
	@Override
	public void run() {		
		while (true) {
			if (running) {
				if (!processing) {
					//if not processing something, check for processes
					if (currProcess != null) {
						//set time variables and set processing to true
						currentProcessName = currProcess.getProcessName();
						currentProcessServiceTime = currProcess.getServiceTime() * timeUnit;
						currentServiceTime = 0;
						lastTimeUpdate = System.currentTimeMillis();
						processing = true;
					}
					else {
						//yielding where a loop can potentially do nothing forever prevents the
						//thread from using all processing resources 
						Thread.yield();
					}
				}
				else {
					//create a temp time to subtract the old time from and add this to currentServiceTime. Set current process's service time to newly calculated temp time.
					long temp = System.currentTimeMillis();
					currentServiceTime = currentServiceTime + (temp - lastTimeUpdate);
					lastTimeUpdate = temp;
					
					currProcess.setActualServiceTime(currentServiceTime);
					if (currentServiceTime >= currentProcessServiceTime) {
						//once currentServiceTime reaches currentProcessServiceTime, it is finished. Process Queue is popped
						processing = false;
						currentProcessName = "None";
						
						//notify listeners that a process was finished
						for (ActionListener listener : actionListeners) {
							listener.actionPerformed(new ActionEvent(currProcess, 0, "finished"));
						}
						
						//set current process to null
						currProcess = null;
					}
				}
			}
			else {
				//yielding where a loop can potentially do nothing forever prevents the
				//thread from using all processing resources 
				Thread.yield();
			}
		}
	}
	
	/**
	 * Gets the running state of the processor. The processor can be processing but not running if
	 * it has a current process but the program is paused.
	 * @return running
	 */
	public boolean getRunning() {
		return running;
	}
	
	/**
	 * Sets the running state of the processor. Setting this to false will pause the processor and preserve any variables.
	 * @param b true to run, false to pause
	 */
	public void setRunning(boolean b) {
		running = b;
	}
	
	/**
	 * Gets the processing state of the processor. This can be true even if the processor is not running.
	 * @return true if the processor has a current process, false otherwise
	 */
	public boolean getProcessing() {
		return processing;
	}
	
	/**
	 * Gets the current process name.
	 * @return current process name or "None"
	 */
	public String getCurrentProcessName() {
		return currentProcessName;
	}
	
	/**
	 * Gets the total amount of time required to service the current process.
	 * @return time (ms) or 0 if no current process
	 */
	public long getCurrentProcessServiceTime() {
		return currentProcessServiceTime;
	}
	
	/**
	 * Gets the  amount of time spent servicing the current process so far.
	 * @return time (ms) or 0 if no current process
	 */
	public long getCurrentServiceTime() {
		return currentServiceTime;
	}
	
	/**
	 * Adds an action listener to the action listener list. Action listeners are notified when a process
	 * has been finished. The action source will contain the ProcessSim object and the action command 
	 * will contain "finished".
	 * @param e listener to be added
	 */
	public void addActionListenerToProcessorQueue(ActionListener e) {
		actionListeners.add(e);
	}
	
	/**
	 * Sets the current process of the processor. This should only be done if the
	 * current process is equal to null.
	 * @param currProcess
	 */
	public void setCurrProcess(ProcessSim currProcess) {
		this.currProcess = currProcess;
	}
	
	/**
	 * Gets the current process.
	 * @return current process or null
	 */
	public ProcessSim getCurrProcess() {
		return currProcess;
	}
	
	/**
	 * Sets the time unit of the processor. The time unit is how many milliseconds need to
	 * pass before incrementing the clock.
	 * @param ms milliseconds
	 */
	public void setTimeUnit(int ms) {
		timeUnit = ms;
	}
}
